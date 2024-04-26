package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import net.minecraft.client.Minecraft;

public abstract class Command {
	
	protected static final Minecraft mc = Minecraft.getMinecraft();
	private String name;
	private String[] alias;
	private String description;
	private String usage;
	
	public Command(String name, String[] alias, String description, String usage) {
		this.name = name;
		this.alias = alias;
		this.description = description;
		this.usage = usage;
	}
	
	public Command(String name, String description, String usage) {
		this.name = name;
		this.alias = null;
		this.description = description;
		this.usage = usage;
	}

	public static char SECTIONSIGN = '\u00a7';

	public static String SECTIONSIGNSTRING = "\u00a7";

	public static char SECTIONSIGN() {
		return SECTIONSIGN;
	}

	public abstract void call(String[] args);
	
	public String getName() {
		return this.name;
	}
	
	public String[] getAlias() {
		return this.alias;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getUsage() {
		return Zenith.commandManager.getPrefix() + this.usage;
	}
}
