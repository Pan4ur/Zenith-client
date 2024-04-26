package me.gopro336.zenith.feature.hudElement.hudElement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.api.util.math.AnimationUtil;
import me.gopro336.zenith.api.util.color.ColorHolder;
import me.gopro336.zenith.api.util.color.ColorUtils;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.command.Command;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Gopro336
 */
@AnnotationHelper(name = "ActiveModules", category = Category.HUD)
public class ActiveModulesElement extends Element {
    
    private Property<Boolean> lines = new Property<>(this, "Lines", "", true);
    private Property<Boolean> trans = new Property<>(this, "Trans", "", false);
    private Property<Color> color = new Property<>(this, "Color", "", new Color(255, 62, 154, 255), v -> !trans.getValue());
    private Property<Boolean> pulse = new Property<>(this, "Pulse", "", true);
    private NumberProperty<Float> range = new NumberProperty<>(this, "Range", "", 0.1f, 0.6f, 1f);
    private NumberProperty<Float> spread = new NumberProperty<>(this, "Spread", "", 0.1f, 1.4f, 2f);
    private NumberProperty<Float> speed = new NumberProperty<>(this, "Speed", "", 1f, 1f, 10f);
    private NumberProperty<Float> animationSpeed = new NumberProperty<>(this, "Speed", "", 0f, 3.5f, 5.0f);
    private NumberProperty<Float> saturation = new NumberProperty<>(this, "Saturation", "", 0f, 143f, 255f);
    private NumberProperty<Float> brightness = new NumberProperty<>(this, "Brightness", "", 0f, 215f, 255f);
    private NumberProperty<Integer> delay = new NumberProperty<>(this, "Delay", "", 50, 300, 900);

    public Anchor anchor = Anchor.TOP_RIGHT;

    public ActiveModulesElement(){
        FeatureManager.getModulesStatic()
                .forEach(module -> {
                    if (module.enabled){
                        module.remainingAnimation = 0;
                    } else {
                        module.remainingAnimation = FontUtil.getStringWidth(getModuleDisplay(module));
                    }
                });
    }

    @Override
    public void onRender() {
        super.onRender();

        setAnchor();

        final int[] yDist = {0};
        final int[] counter = {1};

        boolean isTop = anchor == Anchor.TOP_LEFT || anchor == Anchor.TOP_RIGHT;
        boolean isRight = anchor == Anchor.BOTTOM_RIGHT || anchor == Anchor.TOP_RIGHT;

        ArrayList<Feature> modules = FeatureManager.getEnabledVisibleModules();

        setWidth(50);

        modules.stream()
                .sorted(Comparator.comparingInt(module -> isTop ? -(int) FontUtil.getStringWidth(getModuleDisplay(module)) : (int) FontUtil.getStringWidth(getModuleDisplay(module))))
                .forEach(module -> {
                    float stringWidth = FontUtil.getStringWidth(getModuleDisplay(module));
                    String moduleDisplay = getModuleDisplay(module);

                    if (module.enabled && module.remainingAnimation < stringWidth){
                        module.remainingAnimation = AnimationUtil.moveTowards(module.remainingAnimation, stringWidth, (0.01f + animationSpeed.getValue() / 30), 0.1f);
                    }

                    // Background
                    //RenderUtils2D.drawRect(getX() + (isRight ? getWidth() - stringWidth - 2 : 0), getY() + yDist[0], getX() + stringWidth + 2, getY() + (int) (FontUtil.getStringHeight() + 1.5F), new Color(20, 20, 20, 60).getRGB());


                    int color = getColor(counter[0]);

                    // Add optional eye candy line
                    if (lines.getValue()) {
                        //RenderUtils2D.drawRect(getX() + (isRight ? getWidth() - stringWidth - 2 : stringWidth + 2), getY() + yDist[0], getX() + 1F, getY() +(int) (FontUtil.getStringHeight() + 1.5F), color);
                    }

                    if (isRight){
                        FontUtil.drawStringWithShadow(moduleDisplay, (int) ((int) getX() + getWidth() - module.remainingAnimation), (int) (getY() + yDist[0] + 0.5F), module.isVisible() ? color : Color.GRAY.getRGB());

                    } else {
                        FontUtil.drawStringWithShadow(moduleDisplay, (int) ((int) getX() - stringWidth + module.remainingAnimation), (int) (getY() + yDist[0] + 0.5F), module.isVisible() ? color : Color.GRAY.getRGB());
                    }

                    yDist[0] += (int) (FontUtil.getStringHeight() + 1.5F);
                    counter[0]++;
                });

        setHeight(yDist[0]);
    }

    private int getColor(int index) {
        ColorHolder holder = new ColorHolder(color.getValue().hashCode());
        float[] clr = Color.RGBtoHSB((holder.getRawColor() >> 16) & 0xFF, (holder.getRawColor() >> 8) & 0xFF, holder.getRawColor() & 0xFF, null);
        if (trans.getValue()) {
            return getCuteColor(index - 1);
        } else if (pulse.getValue()) {
                return ColorUtils.pulse(index, clr, this.spread.getValue(), this.speed.getValue(), range.getValue()).getRGB();
        } else {
                return ColorUtils.rainbow(delay.getValue() * index, saturation.getValue(), brightness.getValue()).getRGB();
        }
    }

    private int getCuteColor(int index) {

        int size = FeatureManager.getEnabledModules().size();

        int light_blue = new Color(91, 206, 250).getRGB();
        int white = Color.WHITE.getRGB();
        int pink = new Color(245, 169, 184).getRGB();

        int chunkSize = size / 5;

        if (index < chunkSize) {
            return light_blue;
        } else if (index < chunkSize * 2) {
            return pink;
        } else if (index < chunkSize * 3) {
            return white;
        } else if (index < chunkSize * 4) {
            return pink;
        } else if (index < chunkSize * 5) {
            return light_blue;
        }

        return light_blue;
    }

    private void setAnchor() {
        float x = getX() + getWidth() / 2;
        float y = getY() + getHeight() / 2;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (y >= sr.getScaledHeight() / 2F && x >= sr.getScaledWidth() / 2F) {
            anchor = Anchor.BOTTOM_RIGHT;
        } else if (y >= sr.getScaledHeight() / 2F && x <= sr.getScaledWidth() / 2F) {
            anchor = Anchor.BOTTOM_LEFT;
        } else if (y <= sr.getScaledHeight() / 2F && x >= sr.getScaledWidth() / 2F) {
            anchor = Anchor.TOP_RIGHT;
        } else if (y <= sr.getScaledHeight() / 2F && x <= sr.getScaledWidth() / 2F) {
            anchor = Anchor.TOP_LEFT;
        }
    }

    private String getModuleDisplay(Feature module) {
        if (module.getFeatureMetadata() != null) {
            return module.getName()
                    + Command.SECTIONSIGN + "7 "
                    + ChatFormatting.GRAY + "["
                    + ChatFormatting.WHITE
                    + module.getFeatureMetadata()
                    + ChatFormatting.GRAY + "]";
        } else {
            return module.getName();
        }
    }

    public enum Anchor {
        TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}
