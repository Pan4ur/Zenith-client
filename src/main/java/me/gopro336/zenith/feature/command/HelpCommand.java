package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;

public class HelpCommand extends Command {
	
	public HelpCommand() {
		super("Help", new String[]{"?", "wtf"}, "show the help screen", "help");
	}
	
	@Override
	public void call(String[] args) {
		StringBuilder text = new StringBuilder("Avalaible commands:\n");
		for (Command c : Zenith.commandManager.getCommandList()) {
			text.append(c.getName())
				.append(": ")
				.append(c.getUsage())
				.append("\n");
		}
		ChatUtils.message(text.toString());
	}
	
}
