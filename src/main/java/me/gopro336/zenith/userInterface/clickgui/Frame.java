package me.gopro336.zenith.userInterface.clickgui;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.userInterface.clickgui.button.ModuleComponent;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.api.util.color.RenderUtil;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gopro336
 */
public class Frame implements IFrame {
	public String title;
	public final Category category;
	public static int[] counter1 = new int[]{1};
	private boolean isDragging;
	private int bottom;
	private int place;
	private int scroll;
	private final ArrayList<ModuleComponent> mouduleButtonComponents = new ArrayList<>();
	public int width;
	public int height;
	public int posX;
	public int posY;
	public int dragX;
	public int dragY;
	public int topBarHeight;
	public boolean extended = true;
	public static List<Frame> windows = new ArrayList<>();

	public Frame(Category category, int posX, int posY, int width, int height) {
		this.title = category.getName();
		this.isDragging = false;
		this.category = category;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.topBarHeight = height-1;// scuffed

		int yOffset = this.posY + this.height;

		//for each module from the category, initiate a module button and add it to buttons list
		for (Feature feature : FeatureManager.getModules(category)) {
			ModuleComponent button = new ModuleComponent(feature, this.posX, yOffset, this.width, this.height);
			mouduleButtonComponents.add(button);
			yOffset += this.height;

		}
	}

	public void onRender(int mouseX, int mouseY, float partialTicks) {

		// Dragging
		updatePosition(mouseX, mouseY);

		counter1 = new int[]{1};
		//draw top bar
		//Gui.drawRect(X, Y, X + W, (Y + H)-1, (new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.alpha.getValue())).getRGB());//119
		Gui.drawRect(posX, posY, posX + width, (posY + height)/*-1*/+1, ClickGuiFeature.color.getValue().getRGB());//119

//		Zenith.clickGUI.drawGradient(posX, (posY + height)-1, posX + width, posY + height, new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB(), new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB());

		//draw title string
		FontUtil.drawString(category.getName(), posX + 4, posY + 4);

		if (extended) {
			int modY = posY + height;

			//BlurUtil.blurArea(posX, (posY -7)+ height -1, width, preH - posY - 7, 3, 1f, 1f);

			for (ModuleComponent moduleComponent : mouduleButtonComponents) {
				Frame.counter1[0] = counter1[0] + 1;
				//draw moduleButton
				moduleComponent.setPosX(posX);
				moduleComponent.setPosY(modY);
				moduleComponent.onRender(mouseX, mouseY, partialTicks);

				if (moduleComponent.isModuleExtended()){
					//set "dropdown" to reference the dropdown defined inside of moduleButton class
					Dropdown dropdown = moduleComponent.dropdown;
					dropdown.setX(posX);
					dropdown.setY(modY);
					dropdown.onRender(mouseX, mouseY, partialTicks);
					//boost is multiplied by height beforehand
					modY += dropdown.getBoost();
				}
				modY += height;
			}
			RenderUtil.drawRectOutline(posX, posY, posX + width, modY, (ClickGuiFeature.thin.getValue() ? 0.5d : 1d),
					new Color((Math.min(ClickGuiFeature.color.getValue().getRed() + 10, 255)),
							(Math.min(ClickGuiFeature.color.getValue().getGreen() + 10, 255)),
							(Math.min(ClickGuiFeature.color.getValue().getBlue() + 10, 255)),
							(Math.min(ClickGuiFeature.color.getValue().getAlpha() + 10, 255))).getRGB());
		}
	}

//	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
//		boolean withinBounds = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getTopBarHeight());
//
//		switch (mouseButton) {
//			case 0: {
////				if (withinBounds) {
////					setDragging(true);
////					setDragX(getPosX() - mouseX);
////					setDragY(getPosY() - mouseY);
////					return true;
////				}
//				break;
//			}
//			case 1: {
//				if (withinBounds) {
//					setExtended(!isExtended());
//					return true;
//				}
//				break;
//			}
//		}
//
////		if (extended && mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getTotalHeight())) {
////			for(IComponent component : getButtonComponents()) {
////				component.onMouseClicked(mouseX, mouseY, mouseButton);
//////				if(component.onMouseClicked(mouseX, mouseY, mouseButton)) {
//////					return true;
//////				}
////			}
////		}
//
//		for (ModuleComponent moduleComponent : getButtonComponents()) {
//			moduleComponent.onMouseClicked(mouseX, mouseY, mouseButton);
////				if(component.onMouseClicked(mouseX, mouseY, mouseButton)) {
////					return true;
////				}
//			moduleComponent.dropdown.getButtons().forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton));
//		}
//
//		return false;
//	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean withinBounds = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getTopBarHeight());
		switch (mouseButton) {
			case 0: {
//				if (withinBounds) {
//					setDragging(true);
//					setDragX(getPosX() - mouseX);
//					setDragY(getPosY() - mouseY);
//					return true;
//				}
				break;
			}
			case 1: {
				if (withinBounds) {
//					setExtended(!isExtended());
					return true;
				}
				break;
			}
		}
//		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended)
//				.peek(moduleComponent -> moduleComponent.onMouseClicked(mouseX, mouseY, mouseButton)).forEach(
//				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
//						.peek(component -> ChatUtils.error("lick on my cock"))
//						.peek(component -> component.onMouseClicked(mouseX, mouseY, mouseButton))
//						.filter(settingComponent -> settingComponent.isSubSetting)
//						.filter(SettingComponent::isSubNotHidden)
//						.peek(component -> ChatUtils.error("broooo"))
//						.forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton)));

//		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended)
//				.forEach(moduleComponent -> moduleComponent.onMouseClicked(mouseX, mouseY, mouseButton));

		getButtonComponents().stream().peek(moduleComponent -> moduleComponent.onMouseClicked(mouseX, mouseY, mouseButton))
				.filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> !(settingComponent.isSubSetting))
						.filter(SettingComponent::isNotHidden)
						.forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton)));
		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> settingComponent.isSubSetting)
						.filter(SettingComponent::isSubNotHidden)
						.forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton)));
		return false;
	}

	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		getButtonComponents().stream().peek(moduleComponent -> moduleComponent.onMouseReleased(mouseX, mouseY, mouseButton))
				.filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> !(settingComponent.isSubSetting))
						.filter(SettingComponent::isNotHidden)
						.forEach(component -> component.onMouseReleased(mouseX, mouseY, mouseButton)));
		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> settingComponent.isSubSetting)
						.filter(SettingComponent::isSubNotHidden)
						.forEach(component -> component.onMouseReleased(mouseX, mouseY, mouseButton)));
	}

	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		getButtonComponents().stream().peek(moduleComponent -> moduleComponent.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick))
				.filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> !(settingComponent.isSubSetting))
						.filter(SettingComponent::isNotHidden)
						.forEach(component -> component.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)));
		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> settingComponent.isSubSetting)
						.filter(SettingComponent::isSubNotHidden)
						.forEach(component -> component.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)));
	}

	public void onKeyTyped(char character, int keyCode) {
		getButtonComponents().stream().peek(moduleComponent -> moduleComponent.onKeyTyped(character, keyCode))
				.filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> !(settingComponent.isSubSetting))
						.filter(SettingComponent::isNotHidden)
						.forEach(component -> component.onKeyTyped(character, keyCode)));
		getButtonComponents().stream().filter(moduleComponent -> moduleComponent.extended).forEach(
				moduleComponent -> moduleComponent.dropdown.getButtons().stream()
						.filter(settingComponent -> settingComponent.isSubSetting)
						.filter(SettingComponent::isSubNotHidden)
						.forEach(component -> component.onKeyTyped(character, keyCode)));
	}

	private int getTotalHeight() {
		int currentHeight = getTopBarHeight();
		if (!extended) return currentHeight;

		for (ModuleComponent component : getButtonComponents()) {
			currentHeight += component.getHeight();
			if (component.isModuleExtended())
				currentHeight += component.dropdown.getBoost();
		}

		return currentHeight;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setPosX(mouseX - dragX);
			this.setPosY(mouseY - dragY);
		}
	}

	public void onUpdate() {
		if (!extended) return;
		for (ModuleComponent button : mouduleButtonComponents) {
			button.dropdown.onUpdate();
		}

	}

	public ArrayList<ModuleComponent> getButtonComponents() {
		return mouduleButtonComponents;
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= posX && x <= posX + width && y >= posY && y <= posY + height;
	}

	public void setDragging(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isDragging() {
		return isDragging;
	}

	private boolean mouseWithinBounds(int X, int Y, int W, int H, int mX, int mY) {
		return (mX >= X && mX <= X + W && mY >= Y && mY <= Y + H);
	}

	public int getDragX() {
		return dragX;
	}

	public void setDragX(int dragX) {
		this.dragX = dragX;
	}

	public int getDragY() {
		return dragY;
	}

	public void setDragY(int dragY) {
		this.dragY = dragY;
	}

	public int getPosY() {
		return posY;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPosX() {
		return posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setExtended(boolean open) {
		this.extended = open;
	}

	public boolean isExtended(){
		return extended;
	}

	public int getTopBarHeight() {
		return topBarHeight;
	}
}