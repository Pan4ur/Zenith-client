package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.api.util.time.OldTimer;
import me.gopro336.zenith.asm.mixin.imixin.ISPacketPlayerPosLook;
import me.gopro336.zenith.asm.mixin.mixins.accessor.IEntityPlayerSP;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.player.PushOutOfBlocksEvent;
import me.gopro336.zenith.event.world.TickEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

// mega paste
@AnnotationHelper(name = "Burrow", description = "Fills your own hole", category = Category.EXPLOIT)
public class Burrow
extends Feature {
    public Property<Boolean> rotate = new Property<>(this, "Rotate", "", true);
    public Property<Boolean> swing = new Property<>(this, "Swing", "", true);
    public Property<Boolean> strict = new Property<>(this, "Strict", "", false);
    public Property<Boolean> skulls = new Property<>(this, "Skulls", "", true);
    //public static Property<Class443> customBlocks = new Property<>(this, "CustomBlocks", new Class443(new String[0]));
    //public static Property<FilterMode> filter = new Property<>(this, "Filter", FilterMode.NONE);
    public CurrentStep Field294 = CurrentStep.WAITING;
    public OldTimer Field295 = new OldTimer();

    public enum CurrentStep{
        WAITING,
        DISABLING
    }
    
    public enum FilterMode{
        NONE, WHITELIST, BLACKLIST
    }

    @Listener
    public void Method461(PushOutOfBlocksEvent pushOutOfBlocksEvent) {
        pushOutOfBlocksEvent.setCanceled(true);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            this.toggle();
            return;
        }
        if (!mc.player.onGround) {
            this.toggle();
            return;
        }
        this.Field294 = CurrentStep.WAITING;
    }

    @Listener
    public void Method131(PacketReceiveEvent packetEvent) {
        block1: {
            if (mc.currentScreen instanceof GuiDownloadTerrain) {
                this.toggle();
                return;
            }
            if (!(packetEvent.getPacket() instanceof SPacketPlayerPosLook) || ((Boolean)this.strict.getValue()).booleanValue()) break block1;
            ((ISPacketPlayerPosLook) packetEvent.getPacket()).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook) packetEvent.getPacket()).setPitch(mc.player.rotationPitch);
        }
    }

    @Listener
    public void Method462(TickEvent tickEvent) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.Field294 == CurrentStep.DISABLING) {
            if (this.Field295.GetDifferenceTiming(500.0)) {
                this.toggle();
            }
            return;
        }
        if (!mc.player.onGround) {
            this.toggle();
            return;
        }
        if (mc.world.getBlockState(new BlockPos((Entity) mc.player)).getBlock() == Blocks.AIR) {
            if (((Boolean)this.skulls.getValue()).booleanValue() && mc.world.getBlockState(new BlockPos((Entity) mc.player).up(2)).getBlock() != Blocks.AIR) {
                if (this.getBlockInHotbar() == -1) {
                    this.toggle();
                    return;
                }
                BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                BlockPos blockPos2 = blockPos.down();
                EnumFacing enumFacing = EnumFacing.UP;
                Vec3d vec3d = new Vec3d((Vec3i)blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing.getDirectionVec()).scale(0.5));
                if (((Boolean)this.rotate.getValue()).booleanValue()) {
                    if (((IEntityPlayerSP) mc.player).getLastReportedPitch() < 0.0f) {
                        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(mc.player.rotationYaw, 0.0f, true));
                    }
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, 90.0f, true));
                    ((IEntityPlayerSP) mc.player).setLastReportedPosY(mc.player.posY + 1.16);
                    ((IEntityPlayerSP) mc.player).setLastReportedPitch(90.0f);
                }
                float f = (float)(vec3d.x - (double)blockPos.getX());
                float f2 = (float)(vec3d.y - (double)blockPos.getY());
                float f3 = (float)(vec3d.z - (double)blockPos.getZ());
                boolean bl = mc.player.inventory.currentItem != this.getBlockInHotbar();
                int n = mc.player.inventory.currentItem;
                if (bl) {
                    mc.player.inventory.currentItem = this.getBlockInHotbar();
                    mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.getBlockInHotbar()));
                }
                mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos2, enumFacing, EnumHand.MAIN_HAND, f, f2, f3));
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                }
                if (bl) {
                    mc.player.inventory.currentItem = n;
                    mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                }
                this.Field295.UpdateCurrentTime();
                this.Field294 = CurrentStep.DISABLING;
                return;
            }
            if (this.Method464() == -1) {
                this.toggle();
                return;
            }
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            BlockPos blockPos3 = blockPos.down();
            EnumFacing enumFacing = EnumFacing.UP;
            Vec3d vec3d = new Vec3d((Vec3i)blockPos3).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing.getDirectionVec()).scale(0.5));
            if (((Boolean)this.rotate.getValue()).booleanValue()) {
                if (((IEntityPlayerSP) mc.player).getLastReportedPitch() < 0.0f) {
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(mc.player.rotationYaw, 0.0f, true));
                }
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, 90.0f, true));
                ((IEntityPlayerSP) mc.player).setLastReportedPosY(mc.player.posY + 1.16);
                ((IEntityPlayerSP) mc.player).setLastReportedPitch(90.0f);
            }
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, false));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75, mc.player.posZ, false));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.01, mc.player.posZ, false));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16, mc.player.posZ, false));
            if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("crystalpvp")) {
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16, mc.player.posZ, false));
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24, mc.player.posZ, false));
            }
            float f = (float)(vec3d.x - (double)blockPos.getX());
            float f4 = (float)(vec3d.y - (double)blockPos.getY());
            float f5 = (float)(vec3d.z - (double)blockPos.getZ());
            boolean bl = mc.player.inventory.currentItem != this.Method464();
            int n = mc.player.inventory.currentItem;
            if (bl) {
                mc.player.inventory.currentItem = this.Method464();
                mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.Method464()));
            }
            mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos3, enumFacing, EnumHand.MAIN_HAND, f, f4, f5));
            if (((Boolean)this.swing.getValue()).booleanValue()) {
                mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
            if (bl) {
                mc.player.inventory.currentItem = n;
                mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
            }
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, this.Method463(), mc.player.posZ, false));
            this.Field295.UpdateCurrentTime();
            this.Field294 = CurrentStep.DISABLING;
        } else {
            this.toggle();
        }
    }

    public double Method463() {
        BlockPos blockPos;
        if (mc.getCurrentServerData() != null) {
            if (mc.getCurrentServerData().serverIP.toLowerCase().contains("crystalpvp") || mc.getCurrentServerData().serverIP.toLowerCase().contains("2b2t")) {
                blockPos = new BlockPos(mc.player.posX, mc.player.posY + 2.34, mc.player.posZ);
                if (mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir) {
                    return mc.player.posY + 2.34;
                }
            } else {
                if (mc.getCurrentServerData().serverIP.toLowerCase().contains("endcrystal")) {
                    if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 4.0, mc.player.posZ)).getBlock() instanceof BlockAir) {
                        return mc.player.posY + 4.0;
                    }
                    return mc.player.posY + 3.0;
                }
                if (mc.getCurrentServerData().serverIP.toLowerCase().contains("netheranarchy")) {
                    if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 8.5, mc.player.posZ)).getBlock() instanceof BlockAir) {
                        return mc.player.posY + 8.5;
                    }
                    return mc.player.posY + 9.5;
                }
                if (mc.getCurrentServerData().serverIP.toLowerCase().contains("9b9t")) {
                    BlockPos blockPos2 = new BlockPos(mc.player.posX, mc.player.posY + 9.0, mc.player.posZ);
                    if (mc.world.getBlockState(blockPos2).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos2.up()).getBlock() instanceof BlockAir) {
                        return mc.player.posY + 9.0;
                    }
                    for (int i = 10; i < 20; ++i) {
                        BlockPos blockPos3 = new BlockPos(mc.player.posX, mc.player.posY + (double)i, mc.player.posZ);
                        if (!(mc.world.getBlockState(blockPos3).getBlock() instanceof BlockAir) || !(mc.world.getBlockState(blockPos3.up()).getBlock() instanceof BlockAir)) continue;
                        return mc.player.posY + (double)i;
                    }
                    return mc.player.posY + 20.0;
                }
            }
        }
        if (mc.world.getBlockState(blockPos = new BlockPos(mc.player.posX, mc.player.posY - 9.0, mc.player.posZ)).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir) {
            return mc.player.posY - 9.0;
        }
        for (int i = -10; i > -20; --i) {
            BlockPos blockPos4 = new BlockPos(mc.player.posX, mc.player.posY - (double)i, mc.player.posZ);
            if (!(mc.world.getBlockState(blockPos4).getBlock() instanceof BlockAir) || !(mc.world.getBlockState(blockPos4.up()).getBlock() instanceof BlockAir)) continue;
            return mc.player.posY - (double)i;
        }
        return mc.player.posY - 24.0;
    }

    public int Method464() {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)itemStack.getItem()).getBlock();
            //if (filter.getValue() == FilterMode.BLACKLIST ? ((Class443)customBlocks.getValue()).Method682().contains(block) : filter.getValue() == FilterMode.WHITELIST && !((Class443)customBlocks.getValue()).Method682().contains(block)) continue;
            n = i;
            break;
        }
        return n;
    }

    public int getBlockInHotbar() {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemSkull)) continue;
            n = i;
            break;
        }
        return n;
    }
}