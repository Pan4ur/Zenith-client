package me.gopro336.zenith.api.util.newRender;

import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class ColorUtil {

	public static Color alphaCycle(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F - 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	public static int makeBrighter(int color) {
		Color c = new Color(color, true);
		return ColorUtil.toInt(c.brighter());
	}
	
	public static int makeDarker(int color) {
		Color c = new Color(color, true);
		return ColorUtil.toInt(c.darker());
	}
	
	public static int toInt(Color color) {
		return (color.getRed() << 16) + (color.getGreen() << 8) + (color.getBlue() << 0) + (color.getAlpha() << 24);
	}

	public static void setColor(Color color) {
		GL11.glColor4d(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
	}

	public static Color staticRainbow() {
		float hue = (float) (System.currentTimeMillis() % 11520L) / 12000.0f;
		int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
		int red = rgb >> 16 & 255;
		int green = rgb >> 8 & 255;
		int blue = rgb & 255;
		return new Color(red, green, blue);
	}

	public static Color dynamicRainbow(long offset, float brightness, int speed) {
		float hue = (float) (System.nanoTime() + offset * speed) / 1.0E10f % 1.0f;
		int color = (int) Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, brightness, 1.0f)), 16);
		return new Color(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, new Color(color).getAlpha() / 255.0f);
	}

	public static Color alphaIntegrate(Color color, float alpha) {
		float red = (float) color.getRed() / 255;
		float green = (float) color.getGreen() / 255;
		float blue = (float) color.getBlue() / 255;
		return new Color(red, green, blue, alpha);
	}

	public static Color alphaStep(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F - 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	public static int toRGBA(int r, int g, int b, int a) {
		return (r << 16) + (g << 8) + (b) + (a << 24);
	}
}
