package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.asm.mixin.imixin.ICPacketPlayer;
import me.gopro336.zenith.event.EventStageable;
import me.gopro336.zenith.event.entity.PlayerUseItemEvent;
import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.event.player.MoveInputEvent;
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent;
import me.gopro336.zenith.event.world.UpdateEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "NoSlow", description = "Makes you not slow down while i. e. eating", category = Category.MOVEMENT)
public class NoSlow extends Feature {
    private final Property<Boolean> glide = new Property<>(this, "Glide", "", false);
    private final Property<Boolean> strict = new Property<>(this, "Strict", "", false);
    private final Property<Boolean> airStrict = new Property<>(this, "AirStrict", "", false);
    private final Property<Boolean> ncp = new Property<>(this, "NCP", "", false);

    private boolean serverSneaking;
    private boolean gliding;

    @Listener
    public void onMoveInput(MoveInputEvent event) {
        if(mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getInput().moveStrafe *= 5;
            event.getInput().moveForward *= 5;
        }
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.world == null) return;

        Item item = mc.player.getActiveItemStack().getItem();
        if (((!mc.player.isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
            if (serverSneaking && strict.getValue()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                serverSneaking = false;
            }
            if (gliding) {
                gliding = false;
            }
        }
    }

    @Listener
    public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent.Pre event) {

        if (mc.world == null || mc.player == null) return;

        if (mc.player.getActiveItemStack().getItem() instanceof ItemBow) return;

        if (ncp.getValue() && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @Listener
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent.Post event) {

        if (mc.world == null || mc.player == null) return;

        if (mc.player.getActiveItemStack().getItem() instanceof ItemBow) return;
        
        if (ncp.getValue() && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
        }
    }

    @Listener
    public void onPlayerUseItemEvent(PlayerUseItemEvent event) {
        if (mc.player.getActiveItemStack().getItem() instanceof ItemBow) return;

        if (glide.getValue()) {
            if (!gliding) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, false));
            }
            gliding = true;
        }

        if (!serverSneaking && strict.getValue() && (!airStrict.getValue() || !mc.player.onGround || glide.getValue())) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            serverSneaking = true;
        }
    }

    @Listener
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && gliding) {
            ((ICPacketPlayer) event.getPacket()).setOnGround(false);
        }
    }
}
