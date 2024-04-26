package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.api.util.font.CustomFontRenderer;
import me.gopro336.zenith.api.util.font.FontUtil;

import java.awt.*;

@AnnotationHelper(name = "CustomFont", description = "Use a custom font render instead of Minecraft's default", category = Category.RENDER)
public class CustomFont extends Feature {
    public static NumberProperty<Integer> fontsise;
    public static NumberProperty<Integer> yoffset;

    public CustomFont() {
        fontsise = new NumberProperty<>(this, "FontSize", "", 0, 20, 50)
                .onChanged(integerOnChangedValue -> updateFont("Verdana", 0, fontsise.getValue(), true, true));
        yoffset = new NumberProperty<>(this, "Y-Offset", "", -8, 0, 8);
    }

    public static void updateFont(String newName, int style, int size, boolean antialias, boolean metrics) {
        try{
            FontUtil.customFontRenderer = new CustomFontRenderer(new Font("Arial", Font.PLAIN, size), true, false);
            //FontUtil.xFontRenderer = new XFontRenderer(new Font(FontUtil.getCorrectFont(newName), style, size * 2), antialias, 8);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
    }
}

