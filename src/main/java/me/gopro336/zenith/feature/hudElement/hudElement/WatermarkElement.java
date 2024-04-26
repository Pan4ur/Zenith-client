package me.gopro336.zenith.feature.hudElement.hudElement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.color.ColorHolder;
import me.gopro336.zenith.api.util.color.ColorUtils;
import me.gopro336.zenith.feature.command.Command;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;

import java.awt.*;

@AnnotationHelper(name = "Watermark", category = Category.HUD)
public class WatermarkElement extends Element {

    private Property<Boolean> colorSync = new Property<>(this, "ColorSync", "", false);

    private Property<mode> type = new Property<>(this, "Type", "", mode.Solid);
    private Property<Color> color = new Property<>(this, "Color", "", new Color(255, 62, 154, 255), v -> !type.getValue().equals(mode.Rainbow) && colorSync.getValue().equals(false));

    private NumberProperty<Float> range = new NumberProperty<>(this, "Range", "", 0.1f, 1f, 1f, v -> type.getValue().equals(mode.Pulse) && colorSync.getValue().equals(false));
    private NumberProperty<Float> spread = new NumberProperty<>(this, "Spread", "", 0.1f, 1f, 2f, v -> type.getValue().equals(mode.Pulse) && colorSync.getValue().equals(false));
    private NumberProperty<Float> speed = new NumberProperty<>(this, "Speed", "", 1f, 1f, 10f, v -> type.getValue().equals(mode.Pulse) && colorSync.getValue().equals(false));

    private NumberProperty<Float> saturation = new NumberProperty<>(this, "Saturation", "", 0f, 143f, 255f, v -> type.getValue().equals(mode.Rainbow) && colorSync.getValue().equals(false));
    private NumberProperty<Float> brightness = new NumberProperty<>(this, "Brightness", "", 0f, 215f, 255f, v -> type.getValue().equals(mode.Rainbow) && colorSync.getValue().equals(false));
    private NumberProperty<Integer> delay = new NumberProperty<>(this, "Delay", "", 50, 300, 900, v -> type.getValue().equals(mode.Rainbow) && colorSync.getValue().equals(false));

    private enum mode {
        Solid,
        Pulse,
        Rainbow
    }

    String watermarkString =
            Zenith.name
            + "-"
            + Zenith.version;

    public WatermarkElement() {
        setWidth(FontUtil.getStringWidth(watermarkString));
        setHeight(FontUtil.getFontHeight());
    }

    @Override
    public void onRender() {
        if (colorSync.getValue()){
            // todo Make this use a synced color picker
            FontUtil.drawStringWithShadow(watermarkString, getX(), getY(), 0xffffff);
        } else {
            FontUtil.drawStringWithShadow(watermarkString, getX(), getY(), getColor(1));
        }
    }

    private int getColor(int index) {
        ColorHolder holder = new ColorHolder(color.getValue().hashCode());
        float[] clr = Color.RGBtoHSB((holder.getRawColor() >> 16) & 0xFF, (holder.getRawColor() >> 8) & 0xFF, holder.getRawColor() & 0xFF, null);
        if (type.getValue().equals(mode.Solid)) {
            return holder.getColor();
        } else if (type.getValue().equals(mode.Pulse)) {
            return ColorUtils.pulse(index, clr, this.spread.getValue(), this.speed.getValue(), range.getValue()).getRGB();
        } else {
            return ColorUtils.rainbow(delay.getValue() * index, saturation.getValue(), brightness.getValue()).getRGB();
        }
    }
}
