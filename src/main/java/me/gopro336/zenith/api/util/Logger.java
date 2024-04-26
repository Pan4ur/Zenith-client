package me.gopro336.zenith.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public final class Logger {
	private static Logger logger = null;

	public void print(String message) {
		System.out.println(String.format("[%s] %s", "Zenith", message));
	}

	public void printToChat(String message) {
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("§c[%s] §7%s", "Zenith", message.replace("&", "§"))).setStyle(new Style().setColor(TextFormatting.GRAY)));
	}

	public static Logger getLogger() {
		return logger == null ? (logger = new Logger()) : logger;
	}
}

