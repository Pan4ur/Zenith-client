package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import org.lwjgl.input.Keyboard;

/**
 * @author Robeart
 */
public class BindCommand extends Command {
	
	public BindCommand() {
		super("Bind", new String[]{"b", "bd"}, "Bind a module", "bind [module] {key}");
	}
	
	@Override
	public void call(String[] args) {
		if (args.length < 2) {
			ChatUtils.message("Please specify a module/key!");
			return;
		}
		Feature feature = FeatureManager.getModule(args[0]);
		if (feature == null) {
			ChatUtils.message("Can't find module " + args[0]);
			return;
		}
		feature.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
		ChatUtils.message("Set keybind of " + feature.getName() + " to " + feature.getKey());
	}
}
