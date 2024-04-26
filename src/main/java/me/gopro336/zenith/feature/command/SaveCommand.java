package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;

/**
 * @author Robeart
 */
public class SaveCommand extends Command {
	
	public SaveCommand() {
		super("Save", new String[]{"safe"}, "Save the config", "save");
	}
	
	
	@Override
	public void call(String[] args) {
		Zenith.save();
	}
	
}
