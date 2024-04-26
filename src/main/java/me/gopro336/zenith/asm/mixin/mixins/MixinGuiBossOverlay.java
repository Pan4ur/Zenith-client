package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.event.render.BossOverlayEvent;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo info) {
        BossOverlayEvent bossOverlayEvent = new BossOverlayEvent();
        Zenith.INSTANCE.getEventManager().dispatchEvent(bossOverlayEvent);

        if (bossOverlayEvent.isCanceled())
            info.cancel();
    }
}
