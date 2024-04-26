package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.api.util.player.PlayerUtil;
import me.gopro336.zenith.asm.mixin.imixin.ISPacketPlayerPosLook;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.event.player.PlayerDismountEvent;
import me.gopro336.zenith.event.player.PlayerTravelEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.concurrent.atomic.AtomicBoolean;

@AnnotationHelper(name = "BoatFly", category = Category.MOVEMENT, description = "")
public class BoatFly extends Feature {
    
    public Property<Boolean> fixYaw = new Property<>(this, "FixYaw", "", true);
    public Property<Boolean> antiKick = new Property<>(this, "AntiKick", "", true);
    public Property<Boolean> confirm = new Property<>(this, "Confirm", "", false);
    public Property<Boolean> bypass = new Property<>(this, "Bypass", "", true);
    public Property<Boolean> semi = new Property<>(this, "Semi", "", true);
    public Property<Boolean> constrict = new Property<>(this, "Constrict", "", false);
    public NumberProperty<Float> speed = new NumberProperty<>(this, "Speed", "", 0.1f, 1.0f, 50.0f);
    public NumberProperty<Float> vSpeed = new NumberProperty<>(this, "VSpeed", "", 0.0f, 0.5f, 10.0f);
    public NumberProperty<Integer> safetyFactor = new NumberProperty<>(this, "SafetyFactor", "", 0, 2, 10);
    public NumberProperty<Integer> maxSetbacks = new NumberProperty<>(this, "maxSetbacks", "", 0, 10, 20);
    public int Field1241;
    public Vec3d vec = null;
    public int Field1243;
    public AtomicBoolean Field1244 = new AtomicBoolean(false);
    public int Field1245 = 0;

    @Listener
    public void onPacket(PacketReceiveEvent packetEvent) {
        if (mc.player == null || mc.world == null) {
            this.toggle();
            return;
        }
        if (packetEvent.getPacket() instanceof SPacketPlayerPosLook && mc.player.isRiding()) {
            SPacketPlayerPosLook sPacketPlayerPosLook = (SPacketPlayerPosLook)packetEvent.getPacket();
            ((ISPacketPlayerPosLook)sPacketPlayerPosLook).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook)sPacketPlayerPosLook).setPitch(mc.player.rotationPitch);
            sPacketPlayerPosLook.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
            sPacketPlayerPosLook.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            this.Field1241 = sPacketPlayerPosLook.getTeleportId();
            if ((int)maxSetbacks.getValue() > 0) {
                if (this.vec == null) {
                    this.vec = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                    this.Field1243 = 1;
                }
                else if (PlayerUtil.isPressingMoveBinds() && this.Method1221(this.vec, new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ())) < (float)speed.getValue() * 0.8) {
                    this.vec = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                    ++this.Field1243;
                }
                else if ((mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) && this.Method1223(this.vec, new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ())) < (float)vSpeed.getValue() * 0.5) {
                    this.vec = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                    ++this.Field1243;
                }
                else if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && (this.Method1223(this.vec, new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ())) < 0.02 || this.Method1223(this.vec, new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ())) > 1.0)) {
                    this.vec = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                    ++this.Field1243;
                }
                else {
                    this.vec = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                    this.Field1243 = 1;
                }
            }
            if (maxSetbacks.getValue() > 0 && this.Field1243 > maxSetbacks.getValue()) {
                return;
            }
            if (mc.player.isEntityAlive() && mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) && !(mc.currentScreen instanceof GuiDownloadTerrain)) {
                if (this.Field1241 <= 0) {
                    this.Field1241 = sPacketPlayerPosLook.getTeleportId();
                    return;
                }
                if (!confirm.getValue()) {
                    mc.player.connection.sendPacket(new CPacketConfirmTeleport(sPacketPlayerPosLook.getTeleportId()));
                }
                packetEvent.setCanceled(true);
            }
        }
        if (packetEvent.getPacket() instanceof SPacketMoveVehicle && mc.player.isRiding()) {
            if (semi.getValue()) {
                this.Field1244.set(true);
            }
            else {
                packetEvent.setCanceled(true);
            }
        }
    }

    public double Method1221(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.z - vec3d2.z;
        return MathHelper.sqrt((double)(d * d + d2 * d2));
    }

    @Listener
    public void onSendPacket(PacketSendEvent sendPacketEvent) {
        if (mc.player == null || mc.world == null) {
            this.toggle();
            return;
        }
        if (!bypass.getValue()) {
            return;
        }
        if (sendPacketEvent.getPacket() instanceof CPacketVehicleMove) {
            if (mc.player.isRiding() && mc.player.ticksExisted % 2 == 0) {
                mc.playerController.interactWithEntity((EntityPlayer)mc.player, mc.player.getRidingEntity(), constrict.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            }
        }
        else if (sendPacketEvent.getPacket() instanceof CPacketPlayer.Rotation && mc.player.isRiding()) {
            sendPacketEvent.setCanceled(true);
        }
        else if (sendPacketEvent.getPacket() instanceof CPacketInput && (!semi.getValue() || mc.player.ticksExisted % 2 == 0)) {
            sendPacketEvent.setCanceled(true);
        }
    }

    @Listener
    public void Method1222(PlayerDismountEvent dismountEvent) {
        if (!mc.gameSettings.keyBindSneak.isKeyDown()) return;
        dismountEvent.setCanceled(true);
    }

    @Override
    public void onEnable() {
        this.Field1243 = 0;
        this.vec = null;
        this.Field1241 = 0;
        if (mc.player == null || mc.world == null) {
            this.toggle();
        }
    }

    public double Method1223(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d.y - vec3d2.y;
        return MathHelper.sqrt((double)(d * d));
    }

    @Listener
    public void onPlayerTravel(PlayerTravelEvent playerTravelEvent) {
        block15: {
            if (mc.player == null || mc.world == null) {
                this.toggle();
                return;
            }
            if (!(mc.player.getRidingEntity() instanceof EntityBoat)) break block15;
            EntityBoat entityBoat = (EntityBoat) mc.player.getRidingEntity();
            double d = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            if (PlayerUtil.isPressingMoveBinds()) {
                double[] dArray = maths(speed.getValue());
                d = dArray[0];
                d3 = dArray[1];
            } else {
                d = 0.0;
                d3 = 0.0;
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                d2 = vSpeed.getValue();
                if (antiKick.getValue() && mc.player.ticksExisted % 20 == 0) {
                    d2 = -0.04;
                }
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                d2 = -vSpeed.getValue();
            } else if (antiKick.getValue() && mc.player.ticksExisted % 4 == 0) {
                d2 = -0.04;
            }
            if (fixYaw.getValue()) {
                entityBoat.rotationYaw = mc.player.rotationYaw;
            }
            if (safetyFactor.getValue() > 0 && !mc.world.isBlockLoaded(new BlockPos(entityBoat.posX + d * (double)safetyFactor.getValue(), entityBoat.posY + d2 * (double)safetyFactor.getValue(), entityBoat.posZ + d3 * (double)safetyFactor.getValue()), false)) {
                d = 0.0;
                d3 = 0.0;
            }
            if (!semi.getValue() || mc.player.ticksExisted % 2 != 0) {
                if (this.Field1244.get() && semi.getValue()) {
                    entityBoat.setVelocity(0.0, 0.0, 0.0);
                    this.Field1244.set(false);
                } else {
                    entityBoat.setVelocity(d, d2, d3);
                }
            }
            if (confirm.getValue()) {
                ++this.Field1241;
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.Field1241));
            }
            playerTravelEvent.setCanceled(true);
        }
    }

    public static double[] maths(double d) {
        float f = PlayerUtil.mc.player.movementInput.moveForward;
        float f2 = PlayerUtil.mc.player.movementInput.moveStrafe;
        float f3 = PlayerUtil.mc.player.prevRotationYaw + (PlayerUtil.mc.player.rotationYaw - PlayerUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += (float)(f > 0.0f ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += (float)(f > 0.0f ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        double d4 = (double)f * d * d3 + (double)f2 * d * d2;
        double d5 = (double)f * d * d2 - (double)f2 * d * d3;
        return new double[]{d4, d5};
    }
}