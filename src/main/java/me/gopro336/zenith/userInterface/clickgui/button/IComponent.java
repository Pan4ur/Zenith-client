package me.gopro336.zenith.userInterface.clickgui.button;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.color.ColorUtils;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.toggleable.render.RainbowFeature;
import me.gopro336.zenith.userInterface.clickgui.Frame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;

import java.awt.*;
// why did i even make this
public interface IComponent {
    Minecraft mc = Minecraft.getMinecraft();

    default void onRender(int mouseX, int mouseY, float partialTicks) {
    }

    default void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        //return false;
    }

    default void onKeyTyped(char character, int keyCode) {
    }

    default void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    default void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    default void onUpdate(){
    }

    default void onMove() {
    }

    void setExtended(boolean extended);

    boolean isExtended();

    default void drawDescription(){
        FontUtil.drawStringWithShadow(getDescription(), 2, (new ScaledResolution(mc).getScaledHeight() - FontUtil.getFontHeight() - 2), new Color(0xF2C4C4C4, true).getRGB());
    }

    default String getDescription(){
        return "";
    }

    default int getComponentColor(){
        if (RainbowFeature.ClickguiRainbow.getValue()) {
            return ColorUtils.rainbow(
                    Frame.counter1[0] * RainbowFeature.rainbowhue.getValue(), RainbowFeature.Saturation.getValue(), RainbowFeature.Brightness.getValue()).getRGB();
        }
        else {
            return ClickGuiFeature.color.getValue().getRGB();
        }
    }

    default void playButtonSoundEffect(){
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
