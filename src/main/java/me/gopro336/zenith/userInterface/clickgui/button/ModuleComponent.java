package me.gopro336.zenith.userInterface.clickgui.button;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.api.util.newRender.BlurUtil;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.userInterface.clickgui.ClickGUI;
import me.gopro336.zenith.userInterface.clickgui.Dropdown;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Feature;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author Gopro336
 * @since 11/21/2020
 */
public class ModuleComponent implements IComponent {
	private final Feature feature;
	private int posX;
	private int posY;
	private int width;
	private int height;
	public boolean extended;
	public Dropdown dropdown;

	public ModuleComponent(Feature feature, int posX, int posY, int width, int height) {
		this.feature = feature;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.extended = false;

		dropdown = new Dropdown(this.getFeature(), this, this.posX, this.posY, this.width, this.height);
	}

	@Override
	public void onRender(int mouseX, int mouseY, float partialTicks) {
		//draw background
		Zenith.clickGUI.drawGradient(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB(), new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB());


		BlurUtil.blurArea(getPosX(), (getPosY()-7), getWidth(), getHeight() + 7, 8, 3f, 1f);
		Zenith.clickGUI.drawGradient(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(),
				new Color(190, 190, 175, 85).getRGB(),
				new Color(190, 190, 175, 85).getRGB());



		if (feature.isEnabled()) {
			Zenith.clickGUI.drawGradient((getPosX() + 1), (getPosY() + 0.5), (getPosX() + getWidth()) -1, (getPosY() + getHeight()) -0.5, getComponentColor(), getComponentColor());
		}
		else {
			//Zenith.clickGUI.drawGradient((getPosX() + 1), (getPosY() + 0.5), (getPosX() + getWidth()) -1, (getPosY() + getHeight()) -0.5, new Color(16, 16, 16, (ClickGuiFeature.disbuttonalpha.getValue())).getRGB(), new Color(16, 16, 16, (ClickGuiFeature.disbuttonalpha.getValue())).getRGB());
		}
		FontUtil.drawString(feature.getName(), getPosX() + 5, getPosY() + 4);

		if (isHover(getPosX(), getPosY(), getWidth(), getHeight(), mouseX, mouseY)) {
			int hoverAlpha = 80;
			Zenith.clickGUI.drawGradient((getPosX() + 1), (getPosY() + 0.5), (getPosX() + getWidth()), (getPosY() + getHeight()) -0.5, new Color(180, 180, 180, hoverAlpha).getRGB(), new Color(180, 180, 180, hoverAlpha).getRGB());
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		IComponent.super.onMouseClicked(mouseX, mouseY, mouseButton);
		ChatUtils.error("NOOOOOO HOOOOOO MOOOOOOO");
		boolean hovered = isHover(getPosX(), getPosY(), getWidth(), getHeight(), mouseX, mouseY);
		if (hovered) {
			switch (mouseButton) {
				case 0: {
					playButtonSoundEffect();
					getFeature().toggle();
					//return true;
				}
				case 1: {
					playButtonSoundEffect();
					setModuleExtended(!isModuleExtended());
					//return true;
				}
			}
		}
		//return false;
	}

	@Override
	public void setExtended(boolean extended) {

	}

	@Override
	public boolean isExtended() {
		return false;
	}

	private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
		return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setModuleExtended(boolean extended) {
		this.extended = extended;
	}

	public boolean isModuleExtended() {
		return extended;
	}

	public Feature getFeature() {
		return feature;
	}
}
