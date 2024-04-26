package me.gopro336.zenith.event.client

import net.minecraft.client.network.NetworkPlayerInfo

/**
 * @author cookiedragon234 03/Mar/2020
 */
data class GetPlayerTabTextureEvent(val networkPlayerInfo: NetworkPlayerInfo, var shouldLoad: Boolean)
