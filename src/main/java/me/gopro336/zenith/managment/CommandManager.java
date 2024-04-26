package me.gopro336.zenith.managment;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.command.Command;
import me.gopro336.zenith.feature.command.*;
import me.gopro336.zenith.feature.command.DrawnCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
	
	private List<Command> commandList = new ArrayList<>();
	private String prefix = ".";
	
	public CommandManager() {
		//commandList.add(SearchCommand.INSTANCE);
		commandList.add(new ToggleCommand());
		commandList.add(new TeleportCommand());
		commandList.add(new PrefixCommand());
		commandList.add(new PeekCommand());
		//commandList.add(new DiscordRPCEditCommand());
		//commandList.add(new FriendCommand());
		commandList.add(new HelpCommand());
		commandList.add(new SaveCommand());
		commandList.add(new BindCommand());
		commandList.add(new SoftLeaveCommand());
		commandList.add(new SetCommand());
		commandList.add(ModuleListCommand.INSTANCE);
		commandList.add(NbtDumpCommand.INSTANCE);
		commandList.add(new FontCommand());
		//commandList.add(MacroCommand.INSTANCE);
		//commandList.add(WaypointCommand.INSTANCE);
		commandList.add(DrawnCommand.INSTANCE);
		commandList.add(FakePlayerCommand.INSTANCE);
		//commandList.add(SeedCrackerCommand.INSTANCE);
	}

	/**
	 * Inputted string is attempted to be ran as a command
	 *
	 * @param input Input command string
	 */
	public void executeCommand(String input) {
		String commandName = input.contains(" ") ? input.split(Pattern.quote(" "))[0] : input;
		Command command = getCommand(commandName);
		if (command == null) {
			ChatUtils.error("Command [" + commandName + "] not found");
			return;
		}
		String[] args = input.contains(" ") ? input.substring(input.indexOf(" ") + 1).split(" ") : new String[0];
		try {
			command.call(args);
		}
		catch (Throwable t) {
			ChatUtils.error("Error while executing command: " + t.getMessage());
			t.printStackTrace();
		}
		Zenith.save();
	}

	/**
	 * Command aliases are searched for matches to the
	 * input command String. If a match is found, the
	 * Command with that alias is returned
	 *
	 * @param command Input command string
	 */
	public Command getCommand(String command) {
		for (Command command2 : getCommandList()) {
			if (command2.getName().equalsIgnoreCase(command)) return command2;
			if (command2.getAlias() != null) {
				for (String alias : command2.getAlias()) {
					if (alias.equalsIgnoreCase(command)) return command2;
				}
			}
		}
		return null;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public List<Command> getCommandList() {
		return this.commandList;
	}
}
