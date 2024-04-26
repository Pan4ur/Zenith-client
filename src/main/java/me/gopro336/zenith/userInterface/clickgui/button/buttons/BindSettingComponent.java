package me.gopro336.zenith.userInterface.clickgui.button.buttons;

import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.userInterface.clickgui.ClickGUI;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.api.util.font.FontUtil;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

/**
 * @author Gopro336
 */
public class BindSettingComponent extends SettingComponent
{
	private final Feature feature;
	private boolean isBinding;

	public BindSettingComponent(Feature feature, int x, int y, int w, int h, Boolean isSub) {
		super(feature, x, y, w, h, isSub);
		this.feature = feature;
	}

	@Override
	public void onRender(int mouseX, int mouseY, float partialTicks) {
		super.onRender(mouseX, mouseY, partialTicks);

		preComponentRender(false);


		//FontUtil.drawString("Bind", (float) (getPosX() + 6), (float) (getPosY() + 4));


		FontUtil.drawString(isBinding() ? "Press new bind..." : (/*(getFeature().isHold() ? "Hold: " : "Bind: ")*/"Bind: " + GameSettings.getKeyDisplayString(getFeature().getKey())), (int) (getPosX() + 5.0F), (int) (getPosY() + getHeight() / 2.0F - (FontUtil.getStringHeight() / 2) - 0.5F), 0xFFFFFF);
	}

	@Override
	public void onKeyTyped(char character, int keyCode) {
		super.onKeyTyped(character, keyCode);
		if (isBinding()) {
			if (keyCode == Keyboard.KEY_ESCAPE) {
				getFeature().setKey(Keyboard.KEY_NONE);
				setBinding(false);
				return;
			}

			getFeature().setKey(keyCode);
			setBinding(false);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (isBinding()) {
			getFeature().setKey(mouseButton - 100);
			setBinding(false);
			//return true;
		} else {
			boolean withinBounds = isWithinBuffer(mouseX, mouseY);

			if (withinBounds && mouseButton == 0) {
				playButtonSoundEffect();
				setBinding(!isBinding());
				//return true;
			}/* else if (withinBounds && mouseButton == 1) {
				playButtonClick();
				getFeature().setHold(!getFeature().isHold());
				return true;
			}*/
		}
		//return false;
	}

	public boolean isBinding() {
		return isBinding;
	}

	public void setBinding(boolean binding) {
		isBinding = binding;
		ClickGUI.setNoEsc(binding);
	}
}
