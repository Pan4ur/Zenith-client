package me.gopro336.zenith.event.render;

import me.gopro336.zenith.event.EventStageable;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class RebuildChunkEvent extends EventStageable {
	
	private RenderChunk renderChunk;
	
	public RebuildChunkEvent(EventStage stage, RenderChunk renderChunk) {
		super(stage);
		this.renderChunk = renderChunk;
	}
	
	public RenderChunk getRenderChunk() {
		return renderChunk;
	}
	
	public void setRenderChunk(RenderChunk renderChunk) {
		this.renderChunk = renderChunk;
	}
}
