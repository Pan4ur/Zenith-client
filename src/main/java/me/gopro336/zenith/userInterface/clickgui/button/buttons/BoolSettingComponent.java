package me.gopro336.zenith.userInterface.clickgui.button.buttons;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class BoolSettingComponent extends SettingComponent
{
	private final Property<Boolean> value;

	public BoolSettingComponent(Feature feature, Property<Boolean> value, int X, int Y, int W, int H, boolean isSub) {
		super(feature, X, Y, W, H, isSub);
		this.value = value;
	}

	@Override
	public void onRender(int mouseX, int mouseY, float partialTicks) {
		super.onRender(mouseX, mouseY, partialTicks);
		preComponentRender(false);
		if (getProperty().getValue()) drawButton();
		FontUtil.drawString(value.getName(), (float) (getPosX() + 6)+(isSubSetting ? 3 : 0), (float) (getPosY() + 4));
		if (isWithinBuffer(mouseX, mouseY)) {
			int hoverAlpha = 80;
			Zenith.clickGUI.drawGradient((getPosX() + 1), (getPosY() + 0.5), (getPosX() + getWidth()), (getPosY() + getHeight()) -0.5, new Color(180, 180, 180, hoverAlpha).getRGB(), new Color(180, 180, 180, hoverAlpha).getRGB());
		}
		postComponentRender();
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		ChatUtils.error("broooooo");
		boolean withinBounds = isWithinBuffer(mouseX, mouseY);

		if (withinBounds && mouseButton == 0) {
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			getProperty().setValue(!getProperty().getValue());
			//return true;
		} else if (withinBounds && mouseButton == 1) {
			getProperty().setValue(!getProperty().getValue());
			getProperty().toggleOpenState();
		}
		//return false;
	}

	@Override
	public void onUpdate() {
		setHidden(!value.isVisible());
	}

	@Override
	public Property<Boolean> getProperty(){
		return value;
	}
}
