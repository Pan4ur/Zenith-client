package me.gopro336.zenith.feature.toggleable.misc

import me.gopro336.zenith.api.util.newUtil.ChatUtils
import me.gopro336.zenith.event.EventStageable
import me.gopro336.zenith.event.network.PacketExceptionEvent
import me.gopro336.zenith.feature.AnnotationHelper
import me.gopro336.zenith.feature.Category
import me.gopro336.zenith.feature.Feature
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener

@AnnotationHelper(name = "NoCompKick", description = "Stops the exception from being thrown for badly compressed packets", category = Category.MISC)
object NoCompressionKick: Feature() {
	@Listener
	fun onBadPacket(event: PacketExceptionEvent) {
		if (event.stage == EventStageable.EventStage.PRE) {
			event.isCanceled = true
			ChatUtils.message("Prevented packet exception from being thrown")
		} else if (event.stage == EventStageable.EventStage.POST) {
			event.isCanceled = true
			ChatUtils.message("Prevented thrown exception from disconnect")
		}
	}
}
