package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.api.util.newRender.RenderUtils3D;
import me.gopro336.zenith.event.EventStageable;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gopro336
 */

@AnnotationHelper(name = "NewChunks", description = "Marks chunks that are new", category = Category.RENDER)
public class NewChunks extends Feature {
	
	private ICamera frustum = new Frustum();
	
	private Set<ChunkPos> chunks = new HashSet<>();

	@Listener
	public void onReceive(PacketReceiveEvent event) {
		if (event.getStage() != EventStageable.EventStage.PRE) return;
		if (event.getPacket() instanceof SPacketChunkData) {
			final SPacketChunkData packet = (SPacketChunkData) event.getPacket();
			
			//TODO make it find the opposite array of chunks, because packet.isFullChunk() somehow is flagged for everything
			if (packet.isFullChunk()) return;
			
			final ChunkPos newChunk = new ChunkPos(packet.getChunkX(), packet.getChunkZ());
			this.chunks.add(newChunk);
		}
	}
	
	@Listener
	public void onRender(Render3DEvent event) {
		if (mc.getRenderViewEntity() == null) return;
		this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
		
		GlStateManager.pushMatrix();
		RenderUtils3D.beginRender();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.glLineWidth(2f);
		
		for (ChunkPos chunk : this.chunks) {
			final AxisAlignedBB chunkBox = new AxisAlignedBB(chunk.getXStart(), 0, chunk.getZStart(), chunk.getXEnd(), 0, chunk.getZEnd());
			
			
			GlStateManager.pushMatrix();
			if (this.frustum.isBoundingBoxInFrustum(chunkBox)) {
				double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double) event.getPartialTicks();
				double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double) event.getPartialTicks();
				double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double) event.getPartialTicks();
				RenderUtils3D.drawBoundingBox(chunkBox.offset(-x, -y, -z), 214, 86,/*red, green,*/147, 100);
			}
			
			GlStateManager.popMatrix();
		}
		
		GlStateManager.glLineWidth(1f);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();
		RenderUtils3D.endRender();
		GlStateManager.popMatrix();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		chunks.clear();
	}
}
