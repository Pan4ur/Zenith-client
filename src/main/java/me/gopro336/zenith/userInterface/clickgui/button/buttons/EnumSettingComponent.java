package me.gopro336.zenith.userInterface.clickgui.button.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.EnumUtil;
import me.gopro336.zenith.property.Property;
import me.gopro336.zenith.api.util.font.FontUtil;

import java.awt.*;

/**
 * @author Gopro336
 * @since 2/18/2021
 */
public class EnumSettingComponent extends SettingComponent
{
    private final Property<Enum<?>> value;

    public EnumSettingComponent(Feature feature, Property<Enum<?>> value, int X, int Y, int W, int H, Boolean isSub) {
        super(feature, X, Y, W, H, isSub);
        this.value = value;
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        super.onRender(mouseX, mouseY, partialTicks);
        preComponentRender(true);

        FontUtil.drawString(value.getName(), (float) (getPosX() + 6)+(isSubSetting ? 3 : 0), (float) (getPosY() + 4));
        FontUtil.drawString((ChatFormatting.GRAY + value.getValue().toString()), (float) ((getPosX() + getWidth() - 6) - FontUtil.getStringWidth(value.getValue().toString())), (float) (getPosY() + 4));

        if (isSubSetting) RenderUtils2D.drawRect(getPosX()+2, getPosY(), getPosX()+3, getPosY()+ getHeight(), ClickGuiFeature.accentColor.getValue().getRGB());

        if (ClickGuiFeature.dot.getValue() && getSubCount()>0) {
            FontUtil.drawString("...", (float) ((getPosX() + getWidth() - 3) - FontUtil.getStringWidth("...")), (float) (getPosY() + 4), new Color(255, 255, 255, 255).getRGB());
        }
        postComponentRender();
    }

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		boolean forward = mouseX > getPosX() + getWidth() / 2;

        if (isWithinBuffer(mouseX, mouseY)) {
            if (mouseButton == 1) {
                value.toggleOpenState();
                //return true;
            }
		    if (forward){
                EnumUtil.setEnumValue(value, EnumUtil.getNextEnumValue(value, false));
            } else {
                EnumUtil.setEnumValue(value, EnumUtil.getNextEnumValue(value, true));
            }
		    //return true;
		}
		//return false;
	}

    @Override
    public void onUpdate() {
        setHidden(!value.isVisible());
    }

    @Override
    public Property<?> getProperty(){
        return value;
    }

}
