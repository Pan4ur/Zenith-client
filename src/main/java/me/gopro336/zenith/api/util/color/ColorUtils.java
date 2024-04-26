package me.gopro336.zenith.api.util.color;

import me.gopro336.zenith.feature.toggleable.render.RainbowFeature;

import java.awt.*;

public class ColorUtils {

    public static float[] intToRGB(int color) {

        float alpha = ((color >> 24) & 0xFF) / 255f;
        float red = ((color >> 16) & 0xFF) / 255f;
        float green = ((color >> 8) & 0xFF) / 255f;
        float blue = ((color & 0xFF)) / 255f;

        return new float[]{red, green, blue, alpha};
    }

    public static Color rainbow(int delay, float sat, float bright) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), (float) sat / 255.0f, (float) bright / 255.0f);
    }

    public static Color pulse(int delay, float[] hsb, float spread, float speed, float range) {
        double sin = Math.sin(spread * ((System.currentTimeMillis() / Math.pow(10, 2)) * (speed / 10) + delay));
        sin *= range;
        return Color.getHSBColor(hsb[0], hsb[1], (float) ((sin + 1) / 2) + ((1F -range) * 0.5F));
    }

    //add alpha support
    private Color parseHex(String string) {
        if (!string.startsWith("#")) return null;
        String hex = string.toLowerCase().replaceAll("[^0-9a-f]", "");
        if (hex.length() != 6 && hex.length() != 8) return null;

        Color color;
        try {
            if (hex.length() == 8) {

                color = new Color(Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16), Integer.parseInt(hex.substring(6, 8), 16));
            } else {
                color = new Color(Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16));
            }
        }
        catch (NumberFormatException e) {
            return null;
        }

        return color;
    }
}