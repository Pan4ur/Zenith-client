package me.gopro336.zenith.feature.command

import me.gopro336.zenith.api.util.newUtil.ChatUtils
import me.gopro336.zenith.feature.FeatureManager

/**
 * @author Gopro336
 * need to add drawn tho modules first
 */
object DrawnCommand: Command(
	"Drawn",
	arrayOf("d", "visible"),
	"Disable modules being drawn on the arraylist",
	"drawn (module)"
) {
	override fun call(args: Array<String>) {
		if (args.isEmpty()) {
			ChatUtils.message("Please specify a module")
			return
		} else {
			val m = FeatureManager.getModule(args[0])
			if (m == null) ChatUtils.message(args[0] + " is not a valid module")
			else {
				//m.visible = m.visible.not()
				//ChatUtils.message(m.name + " is now " + (if (m.visible) "visible" else "hidden"))
			}
			return
		}
	}
}
