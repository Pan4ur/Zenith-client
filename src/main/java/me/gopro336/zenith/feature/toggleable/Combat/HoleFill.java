package me.gopro336.zenith.feature.toggleable.Combat;

import com.google.common.collect.Lists;
import me.gopro336.zenith.api.util.combat.CrystalUtils;
import me.gopro336.zenith.api.util.newRender.RenderUtils3D;
import me.gopro336.zenith.api.util.newRotations.Placement;
import me.gopro336.zenith.api.util.newRotations.Rotation;
import me.gopro336.zenith.asm.mixin.mixins.accessor.IEntityPlayerSP;
import me.gopro336.zenith.event.client.BlockUtils;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent;
import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.toggleable.exploit.PacketFly;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.handler.ListenerPriority;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@AnnotationHelper(name = "HoleFill", category = Category.COMBAT)
public class HoleFill extends Feature {
    private Property<Boolean> rotate = new Property<>(this, "Rotate", "", true);
    private Property<Boolean> swing = new Property<>(this, "Swing", "", true);

    private NumberProperty<Double> rangeXZ = new NumberProperty<>(this, "Range", "", 5D, 1D, 6D);
    private Property<Boolean> strictDirection = new Property<>(this, "StrictDirection", "", false);

    private Property<Integer> actionShift = new NumberProperty<>(this, "ActionShift", "", 1, 1, 3);
    private NumberProperty<Integer> actionInterval = new NumberProperty<>(this, actionShift, "ActionInterval", "", 0, 0, 5);

    private Property<Boolean> rayTrace = new Property<>(this, "RayTrace", "", false);

    private Property<Boolean> doubleHoles = new Property<>(this, "Double", "", false);

    private Property<Boolean> jumpDisable = new Property<>(this, "JumpDisable", "", false);
    private Property<Boolean> onlyWebs = new Property<>(this, "OnlyWebs", "", false);
    private Property<SmartMode> smartMode = new Property<>(this, "Smart", "", SmartMode.ALWAYS);
    private NumberProperty<Double> targetRange = new NumberProperty<>(this, "EnemyRange", "", 10D, 1D, 15D);
    private Property<Boolean> disableWhenNone = new Property<>(this, "DisableWhenNone", "", false);

    private Property<Boolean> render = new Property<>(this, "Render", "", false);
    private Property<Color> renderColor = new Property<>(this, "RenderColor", "", new Color(229, 11, 137, 181));

    private enum SmartMode {
        NONE, ALWAYS, TARGET
    }

    private Map<BlockPos, Long> renderBlocks = new ConcurrentHashMap<>();

    private Placement placement = null;

    private int itemSlot;
    
    ArrayList<BlockPos> blocks;

    private Map<BlockPos, Long> placedBlocks = new ConcurrentHashMap<>();

    private int tickCounter = 0;

    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        blocks = Lists.newArrayList(BlockPos.getAllInBox(mc.player.getPosition().add(-rangeXZ.getValue(), -rangeXZ.getValue(), -rangeXZ.getValue()), mc.player.getPosition().add(rangeXZ.getValue(), rangeXZ.getValue(), rangeXZ.getValue())));
        tickCounter = actionInterval.getValue();
    }

    @Listener(priority = ListenerPriority.HIGHER)//(priority = 60)
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent.Pre event) {
        placement = null;

        if (jumpDisable.getValue() && mc.player.prevPosY < mc.player.posY) {
            this.toggle();
        }

        //if (event.isCanceled() || !Rotation.canPlaceNormally(rotate.getValue())) return;

        if (FeatureManager.getFeatureByClass(PacketFly.class).isEnabled()) return;

        if (tickCounter < actionInterval.getValue()) {
            tickCounter++;
        }

        if (tickCounter < actionInterval.getValue()) {
            return;
        }

        int slot = getBlockSlot();
        itemSlot = -1;

        if (slot == -1) {
            return;
        }

        blocks = Lists.newArrayList(BlockPos.getAllInBox(mc.player.getPosition().add(-rangeXZ.getValue(), -rangeXZ.getValue(), -rangeXZ.getValue()), mc.player.getPosition().add(rangeXZ.getValue(), rangeXZ.getValue(), rangeXZ.getValue())));

        int ping = CrystalUtils.ping();

        placedBlocks.forEach((pos, time) -> {
            if (System.currentTimeMillis() - time > ping + 100) {
                placedBlocks.remove(pos);
            }
        });

        if (smartMode.getValue() == SmartMode.TARGET && getNearestTarget() == null) return;

        BlockPos pos = StreamSupport.stream(blocks.spliterator(), false)
                .filter(this::isHole)
                .filter(p -> mc.player.getDistance(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5) <= rangeXZ.getValue())
                .filter(p -> Rotation.canPlaceBlock(p, strictDirection.getValue(), rayTrace.getValue(), true))
                .min(Comparator.comparing(e -> MathHelper.sqrt(mc.player.getDistanceSq(e))))
                .orElse(null);

        if (pos != null) {
            placement = Rotation.preparePlacement(pos, rotate.getValue(), false, strictDirection.getValue(), rayTrace.getValue());
            if (placement != null) {
                tickCounter = 0;
                itemSlot = slot;
                renderBlocks.put(pos, System.currentTimeMillis());
                placedBlocks.put(pos, System.currentTimeMillis());
            }
        } else if (disableWhenNone.getValue()) {
            toggle();
        }
    }

    @Listener(priority = ListenerPriority.HIGH)//(priority = 15)
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent.Post event) {
        if (placement != null && itemSlot != -1) {
            boolean changeItem = mc.player.inventory.currentItem != itemSlot;
            int startingItem = mc.player.inventory.currentItem;

            if (changeItem) {
                mc.player.inventory.currentItem = itemSlot;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(itemSlot));
            }

            boolean isSprinting = mc.player.isSprinting();
            boolean shouldSneak = BlockUtils.shouldSneakWhileRightClicking(placement.getNeighbour());

            if (isSprinting) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            if (shouldSneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }

            Vec3d hitVec = new Vec3d(placement.getNeighbour()).add(0.5, 0.5, 0.5).add(new Vec3d(placement.getOpposite().getDirectionVec()).scale(0.5));
            Rotation.rightClickBlock(placement.getNeighbour(), hitVec, EnumHand.MAIN_HAND, placement.getOpposite(), true, swing.getValue());

            double dX = mc.player.posX - ((IEntityPlayerSP) mc.player).getLastReportedPosX();
            double dY = mc.player.posY - ((IEntityPlayerSP) mc.player).getLastReportedPosY();
            double dZ = mc.player.posZ - ((IEntityPlayerSP) mc.player).getLastReportedPosZ();

            boolean positionChanged = dX * dX + dY * dY + dZ * dZ > 9.0E-4D;

            int extraBlocks = 0;
            while (extraBlocks < actionShift.getValue() - 1 && !positionChanged) {
                EntityPlayer nearestTarget = getNearestTarget();
                BlockPos pos = StreamSupport.stream(blocks.spliterator(), false)
                        .filter(this::isHole)
                        .min(Comparator.comparing(e -> (smartMode.getValue() != SmartMode.NONE) && nearestTarget != null ? MathHelper.sqrt(mc.player.getDistanceSq(nearestTarget)) : MathHelper.sqrt(mc.player.getDistanceSq(e))))
                        .orElse(null);
                if (pos != null && Rotation.canPlaceBlock(pos, strictDirection.getValue())) {
                    Placement nextPlacement = Rotation.preparePlacement(pos, rotate.getValue(), true, strictDirection.getValue());
                    if (nextPlacement != null) {
                        Vec3d nextHitVec = new Vec3d(nextPlacement.getNeighbour()).add(0.5, 0.5, 0.5).add(new Vec3d(nextPlacement.getOpposite().getDirectionVec()).scale(0.5));
                        Rotation.rightClickBlock(nextPlacement.getNeighbour(), nextHitVec, EnumHand.MAIN_HAND, nextPlacement.getOpposite(), true, swing.getValue());
                        placedBlocks.put(pos, System.currentTimeMillis());
                        renderBlocks.put(pos, System.currentTimeMillis());
                        extraBlocks++;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }

            if (shouldSneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            if (isSprinting) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }

            if (changeItem) {
                mc.player.inventory.currentItem = startingItem;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(startingItem));
            }
        }
    }

    @Listener
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketBlockChange) {
            if (renderBlocks.containsKey(((SPacketBlockChange) event.getPacket()).getBlockPosition())) {
                if (((SPacketBlockChange) event.getPacket()).getBlockState().getBlock() != Blocks.AIR) {
                    renderBlocks.remove(((SPacketBlockChange) event.getPacket()).getBlockPosition());
                }
            }
        }
    }

    @Listener
    public void onRender(Render3DEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        renderBlocks.forEach((pos, time) -> {
            if (System.currentTimeMillis() - time > 1000) {
                renderBlocks.remove(pos);
            } else {
                if (!render.getValue()) return;
                RenderUtils3D.draw(pos, true, true, 0, 0, renderColor.getValue());
            }
        });
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            if (onlyWebs.getValue()) {
                return ((ItemBlock) item).getBlock() instanceof BlockWeb;
            }
            return true;
        }
        return false;
    }

    private int getBlockSlot() {
        ItemStack stack = mc.player.getHeldItemMainhand();

        if (!stack.isEmpty() && isValidItem(stack.getItem())) {
            return mc.player.inventory.currentItem;
        } else {
            for (int i = 0; i < 9; ++i) {
                stack = mc.player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && isValidItem(stack.getItem())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isHole(BlockPos pos) {
        if (placedBlocks.containsKey(pos)) return false;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityArrow)) {
                return false;
            }
        }
        if (doubleHoles.getValue()) {
            BlockPos twoPos = BlockUtils.validTwoBlockBedrockXZ(pos);
            if (twoPos == null) {
                twoPos = BlockUtils.validTwoBlockObiXZ(pos);
            }
            if (twoPos != null) {
                return true;
            }
        }
        return BlockUtils.isHole(pos);
    }

    private EntityPlayer getNearestTarget() {
        return mc.world.playerEntities.stream()
                .filter(e -> e != mc.player)
                //.filter(e -> !FakePlayerManager.isFake(e))
                //.filter(e -> !Friends.isUUIDFriend(e.getUniqueID().toString()))
                .filter(e -> mc.player.getDistance(e) < targetRange.getValue())
                .min(Comparator.comparing(e -> mc.player.getDistance(e)))
                .orElse(null);
    }
    
}
