package me.gopro336.zenith.userInterface.clickgui.button.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.math.RoundingUtil;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.toggleable.render.RainbowFeature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.api.util.font.FontUtil;
import net.minecraft.util.math.MathHelper;

/**
 * @author Gopro336
 */
public class SliderSettingComponent extends SettingComponent
{
	private final NumberProperty<Number> value;
	private boolean isSliding;

	public SliderSettingComponent(Feature feature, NumberProperty<Number> value, int X, int Y, int W, int H, Boolean isSub) {
		super(feature, X, Y, W, H, isSub);
		this.value = value;
	}

	@Override
	public void onRender(int mouseX, int mouseY, float partialTicks) {
		super.onRender(mouseX, mouseY, partialTicks);

		preComponentRender(false);

		float length = MathHelper.floor((((Number)getProperty().getValue()).floatValue() - ((Number) ((Number) getProperty().getMin())).floatValue()) / (((Number) getProperty().getMax()).floatValue() - ((Number) ((Number) getProperty().getMin())).floatValue()) * (getWidth()-2/*this is here cus of the minibutton*/));

		if (length < 0) {
			getProperty().setValue(getProperty().getMin());
			setSliding(false);
		//} else if (length > getWidth()) {
		} else if (length > (getWidth()-2)) {
			getProperty().setValue(getProperty().getMax());
			setSliding(false);
		}

		if (RainbowFeature.ClickguiRainbow.getValue()) {
			Zenith.clickGUI.drawGradient((getPosX() + 1) + 1+(isSubSetting ? 3 : 0), (getPosY() + 0.5), length+1, (getPosY() + getHeight()) - 1, getComponentColor(), getComponentColor());
		}
		else {
			Zenith.clickGUI.drawGradient((getPosX() + 1) + 1+(isSubSetting ? 3 : 0), (getPosY() + 0.5), length+1, (getPosY() + getHeight()) - 1, ClickGuiFeature.color.getValue().getRGB(), ClickGuiFeature.color.getValue().getRGB());
		}

		FontUtil.drawString(value.getName(), (float) (getPosX() + 6)+(isSubSetting ? 3 : 0), (float) (getPosY() + 4));
		FontUtil.drawString((ChatFormatting.GRAY + String.valueOf(value.getValue())), (float) ((getPosX() + getWidth() - 6+(isSubSetting ? 1 : 0)) - FontUtil.getStringWidth(String.valueOf(value.getValue()))), (float) (getPosY() + 4));


		if (isSliding) {
			if (getProperty().getValue() instanceof Float) {
				float newValue = (mouseX - (getPosX())) * (((Number) getProperty().getMax()).floatValue() - ((Number) getProperty().getMin()).floatValue()) / (getWidth()) + ((Number) getProperty().getMin()).floatValue();

				getProperty().setValue(MathHelper.clamp(RoundingUtil.roundFloat(RoundingUtil.roundToStep(newValue, (float) 0.1/*getProperty().getSteps()*/), 2), (float) getProperty().getMin(), (float) getProperty().getMax()));
			} else if (getProperty().getValue() instanceof Integer) {
				int newValue = (int) ((mouseX - (getPosX())) * (((Number) getProperty().getMax()).intValue() - ((Number) getProperty().getMin()).intValue()) / (getWidth()) + ((Number) getProperty().getMin()).intValue());
				getProperty().setValue(newValue);
			} else if (getProperty().getValue() instanceof Double) {
				double newValue = (mouseX - (getPosX())) * (((Number) getProperty().getMax()).doubleValue() - ((Number) getProperty().getMin()).doubleValue()) / (getWidth()) + ((Number) getProperty().getMin()).doubleValue();

				getProperty().setValue(MathHelper.clamp(RoundingUtil.roundDouble(RoundingUtil.roundToStep(newValue, (double) 0.1/*getProperty().getSteps()*/), 2), (double) getProperty().getMin(), (double) getProperty().getMax()));
			} else if (getProperty().getValue() instanceof Long) {
				long newValue = (long) ((mouseX - (getPosX())) * (((Number) getProperty().getMax()).doubleValue() - ((Number) getProperty().getMin()).doubleValue()) / (getWidth()) + ((Number) getProperty().getMin()).doubleValue());

				getProperty().setValue(newValue);
			}
		}
		postComponentRender();
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int button) {
		super.onMouseClicked(mouseX, mouseY, button);
		if (isWithinBuffer(mouseX, mouseY) && button == 0) {
			setSliding(true);
			//return true;
		}
		//return false;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		super.onMouseReleased(mouseX, mouseY, button);
		if (isSliding()) {
			setSliding(false);
		}
	}

	@Override
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (!isWithinBuffer(mouseX, mouseY)) {
			setSliding(false);
		}
	}

	public void setSliding(boolean sliding) {
		this.isSliding = sliding;
	}

	public boolean isSliding() {
		return this.isSliding;
	}

	@Override
	public void onUpdate() {
		setHidden(!value.isVisible());
	}

	@Override
	public NumberProperty<Number> getProperty(){
		return value;
	}
}
