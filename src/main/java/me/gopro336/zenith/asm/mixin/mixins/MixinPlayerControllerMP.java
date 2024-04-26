package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.asm.mixin.imixin.IMixinPlayerControllerMP;
import me.gopro336.zenith.event.player.StopUsingItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP implements IMixinPlayerControllerMP {

	@Shadow
	public abstract void syncCurrentPlayItem();

	/*@Inject(method = "resetBlockRemoving", at = @At("HEAD"), cancellable = true)
	private void resetBlockRemovingInject(CallbackInfo ci) {
		if (InteractionTweaksModule.INSTANCE.getState() && InteractionTweaksModule.INSTANCE.getStickyBreak()) {
			ci.cancel();
			return;
		}
	}*/
	
	@Invoker("syncCurrentPlayItem")
	@Override
	public abstract void invokeSyncCurrentPlayItem();

	@Inject(method = "onStoppedUsingItem", at = @At("HEAD"), cancellable = true)
	private void onStoppedUsingItemInject(EntityPlayer playerIn, CallbackInfo ci) {
		if (playerIn.equals(Minecraft.getMinecraft().player)) {
			StopUsingItemEvent event = new StopUsingItemEvent();
			Zenith.INSTANCE.getEventManager().dispatchEvent(event);
			if (event.isCanceled()) {
				if (event.isPacket()) {
					this.syncCurrentPlayItem();
					playerIn.stopActiveHand();
				}
				ci.cancel();
			}
		}
	}
}
