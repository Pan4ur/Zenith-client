package me.gopro336.zenith.api.util;

import net.minecraft.client.Minecraft;

public interface IGlobals {
    Minecraft mc = Minecraft.getMinecraft();

    default boolean nullCheck() {
        return mc.player != null || mc.world != null;
    }
}
