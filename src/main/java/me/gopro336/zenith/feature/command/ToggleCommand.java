package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.Feature;

public class ToggleCommand extends Command {
	
	public ToggleCommand() {
		super("Toggle", new String[]{"t", "tl"}, "Toggle a module", "toggle [module], toggle [module] on/off");
	}
	
	@Override
	public void call(String[] args) {
		if (args.length == 0) {
			ChatUtils.message("Please specify a module!");
			return;
		}
		else {
			Feature feature = Zenith.featureManager.getModule(args[0]);
			if (feature == null) {
				ChatUtils.message("Can't find module " + args[0]);
				return;
			}
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off")) {
					boolean value = args[1].equalsIgnoreCase("on");
					feature.setEnabled(value);
				}
				else {
					ChatUtils.message("Invalid argument " + args[1]);
				}
			}
			else feature.toggle();
			String state = feature.isEnabled() ? "\u00a7aenabled" : "\u00a7cdisabled";
			ChatUtils.message(feature.getName() + " has been " + state);
		}
	}
}
