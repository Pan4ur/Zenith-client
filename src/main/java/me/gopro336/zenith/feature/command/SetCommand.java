package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;

/**
 * @author cats
 * Quick set command to make working with smaller or more exact values easier
 */
public class SetCommand extends Command {
	
	public SetCommand() {
		super("Set", new String[]{"s"}, "Sets a value within a module", "set [module] <Property> <value>");
	}
	
	
	public void call(String[] args) {
		if (args.length < 3) {
			ChatUtils.message(this.getUsage());
			return;
		}
		Feature feature = Zenith.featureManager.getModule(args[0]);
		if (feature == null) {
			ChatUtils.message("Can't find module " + args[0]);
			return;
		}
		if (Zenith.SettingManager.getPropertiesByMod(feature).isEmpty()) {
			ChatUtils.message("Module " + args[0] + " has no Properties");
			return;
		}
		for (Property value : Zenith.SettingManager.getPropertiesByMod(feature)) {
			String name = value.getName().replace(" ", "");
			if (name.equalsIgnoreCase(args[1])) {

				try {
					if (value.isBoolean()) {
						value.setValue(Boolean.parseBoolean(args[2]));
						ChatUtils.message("Set " + args[1] + " in " + args[0] + " to " + args[2]);
						return;
					}
					if (value.isNumber()) {

						if (determineNumber(value.getValue()).equalsIgnoreCase("INTEGER")) {
							value.setValue(Integer.parseInt(args[3]));
						}
						else if (determineNumber(value.getValue()).equalsIgnoreCase("FLOAT")) {
							value.setValue(Float.parseFloat(args[3]));
						}
						else if (determineNumber(value.getValue()).equalsIgnoreCase("DOUBLE")) {
							value.setValue(Double.parseDouble(args[3]));
						}
						else if (determineNumber(value.getValue()).equalsIgnoreCase("LONG")) {
							value.setValue(Long.parseLong(args[3]));
						}
						else {
							ChatUtils.message("UNKNOWN NUMBER VALUE");
						}
						ChatUtils.message("Set " + args[1] + " in " + args[0] + " to " + args[2]);
						return;
					}
					if (value.isEnum()) {
//					value.setValue(Enum.parseEnum(args[2]));
						ChatUtils.message("Sorry, Enum Properties are not currently supported by the set command");
						return;
					}
					if (value.isString()) {
						value.setValue(args[2]);
						ChatUtils.message("Set " + args[1] + " in " + args[0] + " to " + args[2]);
						return;
					}
					if (value.isColor()) {
						//value.setValue(args[2]);
						ChatUtils.message("Sorry, Color Properties are not currently supported by the set command");
						return;
					}
				} catch (Exception e){
					ChatUtils.message("An error occurred");
				}
			}
		}
		
		ChatUtils.message("No such Property!");
	}

	public String determineNumber(Object o) {
		if (o instanceof Integer) {
			return "INTEGER";
		}
		else if (o instanceof Float) {
			return "FLOAT";
		}
		else if (o instanceof Double) {
			return "DOUBLE";
		}
		else if (o instanceof Long) {
			return "LONG";
		}
		else {
			return "INVALID";
		}
	}
}
