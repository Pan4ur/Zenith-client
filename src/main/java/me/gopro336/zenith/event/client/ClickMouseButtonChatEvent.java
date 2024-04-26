package me.gopro336.zenith.event.client;

import me.gopro336.zenith.event.EventCancellable;

/**
 * @author Robeart
 */
public class ClickMouseButtonChatEvent extends EventCancellable {
	
	private int mouseX;
	private int mouseY;
	private int mouseButton;
	
	public ClickMouseButtonChatEvent(int mouseX, int mouseY, int mouseButton) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.mouseButton = mouseButton;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}
	
	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
}
