package me.gopro336.zenith.feature.toggleable.misc

import me.gopro336.zenith.feature.AnnotationHelper
import me.gopro336.zenith.feature.Category
import me.gopro336.zenith.feature.Feature
import net.minecraft.client.multiplayer.PlayerControllerMP

/**
 * @author cookiedragon234 25/Jul/2020
 */
@AnnotationHelper(name = "LazyItemSwitch", description = "Spoof your server side item until necessary", category = Category.COMBAT)
object LazyItemSwitch: Feature() {
	fun updatePlayerControllerOnTick(playerController: PlayerControllerMP) {
		if (!this.isEnabled) {
			playerController.updateController()
		} else {
			// Send/receive packets without syncing item as would normally be done each tick
			
			val conn = mc.player.connection
			if (conn.networkManager.isChannelOpen) {
				conn.networkManager.processReceivedPackets()
			} else {
				conn.networkManager.handleDisconnection()
			}
		}
	}
}
