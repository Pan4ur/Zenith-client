package me.gopro336.zenith.asm.mixin.mixins.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface AccessorMinecraft {

    @Accessor("renderPartialTicksPaused")
    float getRenderPartialTicksPaused();
}
