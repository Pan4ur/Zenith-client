package me.gopro336.zenith.event.render;

import me.gopro336.zenith.event.EventCancellable;

/**
 * @author Robeart
 */
public class RenderOverlayEvent extends EventCancellable {
	
	private OverlayType type;
	
	public RenderOverlayEvent(OverlayType type) {
		this.type = type;
	}
	
	public OverlayType getType() {
		return type;
	}
	
	public void setType(OverlayType type) {
		this.type = type;
	}
	
	public enum OverlayType {
		ITEM,
		LIQUID,
		FIRE
	}
	
}
