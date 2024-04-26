package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;

public class PrefixCommand extends Command {
	
	public PrefixCommand() {
		super("Prefix", "Set the prefix", "prefix [prefix]");
	}
	
	@Override
	public void call(String[] args) {
		if (args.length == 0) {
			ChatUtils.message("Please specify what you would like as prefix!");
			return;
		}
		Zenith.commandManager.setPrefix(args[0]);
		ChatUtils.message("Prefix has been changed to: " + args[0]);
	}
}
