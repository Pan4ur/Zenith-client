package me.gopro336.zenith.event.client

import me.gopro336.zenith.api.util.MouseButton
import me.gopro336.zenith.event.EventCancellable

/**
 * @author cookiedragon234 25/Mar/2020
 */
data class ClickMouseButtonEvent(
	val mouseButton: MouseButton
): EventCancellable(EventStage.PRE)

