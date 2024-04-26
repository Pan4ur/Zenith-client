package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author cookiedragon234 12/Nov/2019
 */
@AnnotationHelper(name = "Step", description = "Instantly jumps on a block", category = Category.MOVEMENT)
public class Step extends Feature {
	private final NumberProperty<Integer> stepHeight = new NumberProperty<>(this, "Height", "", 1, 2, 2);
	private final NumberProperty<Integer> entityHeight = new NumberProperty<>(this, "EntityHeight", "", 1, 1, 2);
	private final Property<Boolean> entityStep = new Property<>(this, "EntityStep", "", true);

	public void onUpdate() {
		if (mc.player == null) return;
		
		for (boolean b : new boolean[]{
			mc.player.collidedHorizontally,
			!mc.player.isOnLadder(),
			!mc.player.isInWater(),
			!mc.player.isInLava(),
			mc.player.movementInput.moveStrafe != 0,
			mc.player.movementInput.moveForward != 0,
			!mc.player.movementInput.jump
		}) {
			//System.out.println("b: " + b);
		}
		
		if (
			mc.player != null
				&&
				mc.player.collidedHorizontally
				&&
				!mc.player.isOnLadder()
				&&
				!mc.player.isInWater()
				&&
				!mc.player.isInLava()
				&&
				(
					mc.player.movementInput.moveStrafe != 0
						||
						mc.player.movementInput.moveForward != 0
				)
				&&
				mc.player.onGround
				&&
				!mc.player.movementInput.jump
		) {
			int stepBlocks = stepHeight.getValue();
			
			Vec3d playerHead = mc.player.getPositionVector().add(0, 2, 0);
			
			if (hullCollidesWithBlock(mc.player, playerHead.add(0, 1, 0))) {
				// If theres a block 1 above
				// Return as there isnt enough space
				return;
			}
			
			if (hullCollidesWithBlock(mc.player, playerHead.add(0, 2, 0))) {
				// If theres a block 2 blocks above
				// Only allow to step up 1 block max
				stepBlocks = 1;
			}
			
			double startY = mc.player.posY;
			
			if (stepBlocks == 1) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.42, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.75, mc.player.posZ, true));
			}
			else {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.4, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.75, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.5, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.41, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 0.83, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 1.16, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 1.41, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 1.57, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 1.58, mc.player.posZ, true));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, startY + 1.42, mc.player.posZ, true));
			}
			mc.player.setPosition(mc.player.posX, startY + stepBlocks, mc.player.posZ);
		}
	}
	
	/**
	 * @author Dan @ethug for Seppuku
	 */
	private boolean hullCollidesWithBlock(final Entity entity, final Vec3d nextPosition) {
		final AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
		final Vec3d[] boundingBoxCorners = {
			new Vec3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ),
			new Vec3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ),
			new Vec3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ),
			new Vec3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
		};
		
		final Vec3d entityPosition = entity.getPositionVector().add(0, 2, 0);
		for (final Vec3d entityBoxCorner : boundingBoxCorners) {
			final Vec3d nextBoxCorner = entityBoxCorner.subtract(entityPosition).add(nextPosition);
			final RayTraceResult rayTraceResult = entity.world.rayTraceBlocks(entityBoxCorner,
				nextBoxCorner, true, false, true
			);
			if (rayTraceResult == null)
				continue;
			
			if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
				return true;
		}
		
		return false;
	}
}
