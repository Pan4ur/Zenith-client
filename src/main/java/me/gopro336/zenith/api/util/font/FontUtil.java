package me.gopro336.zenith.api.util.font;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;


public class FontUtil
{
//	public static CustomFontRenderer customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 21), true, false);
	public static CustomFontRenderer customFontRenderer = new CustomFontRenderer(new Font("Arial", Font.PLAIN, 21), true, false);
	public static CustomFontRenderer customChatFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 19), true, false);
	private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

	public static int getStringHeight(){
		//return customFont() ? (customFontRenderer.fontHeight) : fontRenderer.FONT_HEIGHT;
		return fontRenderer.FONT_HEIGHT;
	}

	public static int getStringWidth(String text)
	{
		return customFont() ? (customFontRenderer.getStringWidth(text) + 3) : fontRenderer.getStringWidth(text);
		//return fontRenderer.getStringWidth(text);
	}

	public static void drawString(String text, double X, double Y)
	{
		if (ClickGuiFeature.textShadow.getValue()){
			FontUtil.drawStringWithShadow(text, (int)X, (int)Y, (new Color(ClickGuiFeature.tred.getValue(), ClickGuiFeature.tgreen.getValue(), ClickGuiFeature.tblue.getValue(), ClickGuiFeature.talpha.getValue())).getRGB());
		} else {
			FontUtil.drawString(text, (int)X, (int)Y, (new Color(ClickGuiFeature.tred.getValue(), ClickGuiFeature.tgreen.getValue(), ClickGuiFeature.tblue.getValue(), ClickGuiFeature.talpha.getValue())).getRGB());
		}
	}

	public static void drawString(String text, double x, double y, int color)
	{
		if (customFont())
		{
			customFontRenderer.drawString(text, x, (y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color, false);
			//fontRenderer.drawString(text, (int) (x), (int) (y), color);
			//xFontRenderer.drawStringWithShadow(text, (int)x, (int)(y - 1)+(me.gopro336.zenith.module.render.CustomFont.yoffset.getValue()), color);
		}
		else
		{
			fontRenderer.drawString(text, (int) (x), (int) (y), color);
		}
	}

	public static void drawStringWithShadow(String text, double x, double y, int color)
	{
		if (customFont())
		{
			//customFontRenderer.drawString(text, x, (y - 1)+(me.gopro336.zenith.module.render.CustomFont.yoffset.getValue()), color, false);

			customFontRenderer.drawStringWithShadow(text, x, (y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color);
			//fontRenderer.drawStringWithShadow(text, (float) (x), (float) (y), color);
			//xFontRenderer.drawStringWithShadow(text, (int)x, (int)(y - 1)+(me.gopro336.zenith.module.render.CustomFont.yoffset.getValue()), color);

		}
		else
		{
			fontRenderer.drawStringWithShadow(text, (float) (x), (float) (y), color);
		}
	}
	public static int drawStringInt(String text, double x, double y, int color)
	{
		customChatFontRenderer.drawStringWithShadow(text, x, (y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color);

		return 0;
	}

	public static int drawStringWithShadowInt(String text, double x, double y, int color)
	{
		customChatFontRenderer.drawString(text, (float)x, (float)(y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color);

		return 0;
	}

	public static void drawCenteredStringWithShadow(String text, float x, float y, int color)
	{
		if (customFont())
		{
			customFontRenderer.drawCenteredStringWithShadow(text, x, (y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color);
			//fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2f, y, color);

		}
		else
		{
			fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2f, y, color);
		}
	}

	public static void drawCenteredString(String text, float x, float y, int color)
	{
		if (customFont())
		{
			customFontRenderer.drawCenteredString(text, x, (y - 1)+(me.gopro336.zenith.feature.toggleable.render.CustomFont.yoffset.getValue()), color);
			//customFontRenderer.drawCenteredString(text, (int)(x - getStringWidth(text) / 2), (y - 1)+(me.gopro336.zenith.module.render.CustomFont.yoffset.getValue()), color);
			//fontRenderer.drawString(text, (int) (x - getStringWidth(text) / 2), (int) (y), color);
		}
		else
		{
			fontRenderer.drawString(text, (int) (x - getStringWidth(text) / 2), (int) (y), color);
		}
	}

	public static int getFontHeight()
	{
		return customFont() ? (customFontRenderer.fontHeight / 2) - 1 : fontRenderer.FONT_HEIGHT;
		//return fontRenderer.FONT_HEIGHT;
	}

	public static boolean validateFont(String font)
	{
		for (String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
		{
			if (s.equals(font))
			{
				return true;
			}
		}
		return false;
	}

	public static void setFontRenderer(String stringIn, int size) {
		try{
			if (getCorrectFont(stringIn) == null && !stringIn.equalsIgnoreCase("Zenith")) {
				ChatUtils.error("Invalid font!");
				return;
			}
			customFontRenderer = new CustomFontRenderer(new Font(stringIn, Font.PLAIN, size), true, false);
			//customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, size), true, false);
			//xFontRenderer = new XFontRenderer(new Font(getCorrectFont(stringIn), Font.PLAIN, size), true, 8);
			//xFontRenderer = new XFontRenderer(new Font(getCorrectFont(stringIn), Font.PLAIN, size), true, 8);
		}catch (Exception e) {
			ChatUtils.error(e.toString());
			e.printStackTrace();
		}
	}

	public static String getCorrectFont(String stringIn) {
		for (String s : getFonts()) {
			if (s.equalsIgnoreCase(stringIn)) {
				return s;
			}
		}
		return null;
	}

	public static String[] getFonts() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}

	private static boolean customFont()
	{
		return Zenith.featureManager.getModule("CustomFont").isEnabled();
	}
}
