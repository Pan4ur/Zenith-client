package me.gopro336.zenith.userInterface.clickgui;

import me.gopro336.zenith.api.util.newRender.BlurUtil;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.userInterface.clickgui.button.IComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Gopro336
 * @since 26/11/2020
 */
public class ClickGUI extends GuiScreen
{
	public static ArrayList<Frame> frames = new ArrayList<>();
	private static boolean noEsc = false;

	public ClickGUI() {
		int xOffset = 3;

		for (Category category : Category.values()) {
			if (category == Category.HUD) continue;
			frames.add(new Frame(category, xOffset, 60, 110, 15));
			xOffset += 120;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		frames.forEach(window -> window.onRender(mouseX, mouseY, partialTicks));
		renderTopBar();
	}

	public void renderTopBar(){
		BlurUtil.blurArea(0, (0-7), new ScaledResolution(mc).getScaledWidth(), 40 + 7, 8, 1f, 3f);
		drawGradient(0,0, new ScaledResolution(mc).getScaledWidth_double(), 40,
				new Color(190, 190, 175, 85).getRGB(),
				new Color(190, 190, 175, 85).getRGB());
		//BlurUtil.blurArea(0, (0-7), new ScaledResolution(mc).getScaledWidth(), 40 + 7, 3, 1f, 1f);
//		BlurUtil.boxBlurArea(0, 0, new ScaledResolution(mc).getScaledWidth(), 40);

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		for (Frame f : frames){
			f.onMouseClicked(mouseX, mouseY, mouseButton);
		}

//		for(int i = frames.size() - 1; i >= 0; i--) {
//			Frame frame = frames.get(i);
//			//if(IComponent.onMouseClicked(mouseX, mouseY, mouseButton)) {
//			if(frame.onMouseClicked(mouseX, mouseY, mouseButton)) {
//				Collections.swap(frames, i, frames.size() - 2);
//				break;
//			}
//		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		super.mouseReleased(mouseX, mouseY, mouseButton);
		frames.forEach(frame -> frame.onMouseReleased(mouseX, mouseY, mouseButton));
		for (Frame frame : getFrames()) {
			/*if (frame.getPosX() == frame.getPosX() && frame.getPosY() == frame.getPosY()) {
				frame.setPosX(frame.getPosX() + 10);
				frame.setPosY(frame.getPosY() + 10);
				((Frame) frame).getButtonComponents().forEach(IComponent::onMove);
			}*/
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!noEsc) super.keyTyped(typedChar, keyCode);
		frames.forEach(window -> window.onKeyTyped(typedChar, keyCode));
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		getFrames().forEach(frame -> frame.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		frames.forEach(frame -> frame.setDragging(false));
		ClickGuiFeature.getInstance().disable();
	}

	@Deprecated
	public void drawGradient(double left, double top, double right, double bottom, int startColor, int endColor) {
		drawGradientRect((int)left, (int)top, (int)right, (int)bottom, startColor, endColor);
	}

	public void onUpdate() {
		frames.forEach(Frame::onUpdate);
	}

	private void doScroll() {
		int w = Mouse.getDWheel();
		if (w < 0) {
			for (Frame window : frames) {
				window.setPosY(window.getPosY() - 8);
			}
		}
		else if (w > 0) {
			for (Frame window : frames) {
				window.setPosY(window.getPosY() + 8);
			}
		}
	}

	public static ArrayList<Frame> getFrames() {
		return frames;
	}

	public static Frame getWindowByName(String in) {
		for (Frame w : getFrames()) {
			if (w.title.equalsIgnoreCase(in)) {
				return w;
			}
		}
		return null;
	}

	public static boolean isNoEsc() {
		return noEsc;
	}

	public static void setNoEsc(boolean noEsc) {
		ClickGUI.noEsc = noEsc;
	}
}
