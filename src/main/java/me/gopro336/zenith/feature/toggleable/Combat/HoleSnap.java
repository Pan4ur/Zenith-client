package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.api.util.newRender.RenderUtils3D;
import me.gopro336.zenith.api.util.kotlin.SurroundUtils;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.event.client.BlockUtils;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.player.MoveEvent;
import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static me.gopro336.zenith.api.util.math.NumberUtils.min;
import static me.gopro336.zenith.api.util.kotlin.floor.toRadian;

@AnnotationHelper(name = "HoleSnap", description = "gravitates you twords holes", category = Category.MOVEMENT)
public class HoleSnap extends Feature {

    public NumberProperty<Integer> blockRange = new NumberProperty<>(this, "BlockRange", "", 1, 3, 6);

    private Vec3d Center = Vec3d.ZERO;
    private BlockPos target;

    @Override
    public void onEnable() {
        mc.player.motionX = 0;
        mc.player.motionZ = 0;
        //Center = GetCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    /*@Listener
    public void onInputUpdateEvent(InputUpdateEvent event) {
        if (event.movementInput instanceof MovementInputFromOptions) {
            event.movementInput.resetMove();
        }
    }*/

    @Listener
    public void onPacket(PacketReceiveEvent event){
        if (event.getPacket() instanceof SPacketPlayerPosLook) toggle();
    }

    @Listener
    public void onWorldRender(Render3DEvent event){
        if (target == null) return;
        RenderUtils3D.draw(target, true, true, 1, 3, new Color(255,255, 255, 255));
    }

    /*@Listener
    public void onMotionUpdate(MotionUpdateEvent event){
        if (event.getStage() != EventStageable.EventStage.PRE) return;*/

    @Listener
    public void moveEvent(MoveEvent event){

        if (!mc.player.isEntityAlive()) return;

        if (SurroundUtils.INSTANCE.checkHole(mc.player)
                .equals(SurroundUtils.HoleType.NONE)){ toggle();}

        target = nearestHole();

        if (target != null){
            //Center = new Vec3d(target.x, mc.player.posY, target.z);
            Center = GetCenter(target.getX(), target.getY(), target.getZ());
            double l_MotionX = Center.x-mc.player.posX;
            double l_MotionZ = Center.z-mc.player.posZ;
            mc.player.motionX = l_MotionX/2;
            mc.player.motionZ = l_MotionZ/2;

        } else {
            ChatUtils.error("No valid HoleSnap locations detected");
            toggle();
        }
    }

    /*@Listener
    public void onMove(MoveEvent event){
        BlockPos target = nearestHole();
        //Vec3d Center = new Vec3d(target.x + 0.5, mc.player.posY, target.z + 0.5);
        //Vec3d Center = new Vec3d(target.x, mc.player.posY, target.z);
        Center = GetCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (target != null){
            double l_XDiff = Math.abs(Center.x - mc.player.posX);
            double l_ZDiff = Math.abs(Center.z - mc.player.posZ);

            /*if (l_XDiff <= 0.1 && l_ZDiff <= 0.1) {
                Center = Vec3d.ZERO;
            }
            else {*/
                /*double l_MotionX = Center.x-mc.player.posX;
                double l_MotionZ = Center.z-mc.player.posZ;
                mc.player.motionX = l_MotionX/2;
                mc.player.motionZ = l_MotionZ/2;*/
            //}
        /*} else {
            ChatUtils.error("No valid HoleSnap locations detected");
            toggle();
        }
    }*/

    public BlockPos nearestHole(){
        return findHoles()
                .stream()
                .min(Comparator.comparingDouble(pos -> mc.player.getDistanceSqToCenter(pos)))
                .orElse(null);
    }

    public boolean isUsableHole(BlockPos blockPos){
        if (!(mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR)) return false;
        if (!BlockUtils.isHole(blockPos)) return false;
        if (blockPos.y >= mc.player.posY) return false;
        if ((mc.world.getBlockState(blockPos.up()) != Blocks.AIR)
                || (mc.world.getBlockState(blockPos.up().up()) != Blocks.AIR)) return false;
        return true;
    }

    private List<BlockPos> findHoles() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(new BlockPos(mc.player), blockRange.getValue(), 4, false, true, 0).stream().filter(this::isUsableHole).collect(Collectors.toList()));
        return positions;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public Vec3d GetCenter(double posX, double posY, double posZ)
    {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

}
