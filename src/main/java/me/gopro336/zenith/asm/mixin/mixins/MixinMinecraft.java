package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.asm.mixin.imixin.IMinecraft;
import me.gopro336.zenith.api.util.MouseButton;
import me.gopro336.zenith.event.client.ClickMouseButtonEvent;
import me.gopro336.zenith.event.render.DisplayGuiScreenEvent;
import me.gopro336.zenith.event.world.PreTickEvent;
import me.gopro336.zenith.feature.toggleable.misc.LazyItemSwitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft {
	
	@Shadow public PlayerControllerMP playerController;
	
	@Accessor(value = "rightClickDelayTimer")
	public abstract int getRightClickDelayTimer();
	
	@Accessor(value = "rightClickDelayTimer")
	public abstract void setRightClickDelayTimer(int rightClickDelayTimer);
	
	@Accessor(value = "timer")
	public abstract Timer getTimer();
	
	/*@Inject(method = "runGameLoop", at = @At("RETURN"))
	public void runGameLoop(CallbackInfo callbackInfo) {
		try {
			if (Minecraft.getMinecraft().player == null) {
				return;
			}
			
			try {
				ModuleManager.cyclicBarrier.await();
			}
			catch (Throwable t) {
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}*/
	
	@Inject(method = "displayGuiScreen", at = @At("HEAD"), cancellable = true)
	public void displayGuiScreen(@Nullable GuiScreen guiScreenIn, CallbackInfo ci) {
		DisplayGuiScreenEvent event = new DisplayGuiScreenEvent(guiScreenIn);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		if (event.isCanceled()) ci.cancel();
	}
	
	@Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
	private void onRightClickMouse(CallbackInfo ci) {
		ClickMouseButtonEvent event = new ClickMouseButtonEvent(MouseButton.RIGHT);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		if (event.isCanceled()) {
			ci.cancel();
		}
	}
	
	@Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
	private void onLeftClickMouse(CallbackInfo ci) {
		ClickMouseButtonEvent event = new ClickMouseButtonEvent(MouseButton.LEFT);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		if (event.isCanceled()) {
			ci.cancel();
		}
	}
	
	@Inject(method = "middleClickMouse", at = @At("HEAD"), cancellable = true)
	private void onMiddleClickMouse(CallbackInfo ci) {
		ClickMouseButtonEvent event = new ClickMouseButtonEvent(MouseButton.MIDDLE);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		if (event.isCanceled()) {
			ci.cancel();
		}
	}

	@Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
	private void Method1747(CallbackInfo callbackInfo) {
		PreTickEvent event = new PreTickEvent();
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
	}
	
	/*@Redirect(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
	private boolean redirActive(EntityPlayerSP entityPlayerSP) {
		if (InteractionTweaksModule.INSTANCE.getState() && InteractionTweaksModule.INSTANCE.getMultiTask()) {
			return false;
		}
		
		return entityPlayerSP.isHandActive();
	}
	
	@Redirect(method = "rightClickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"))
	private boolean redirHitting(PlayerControllerMP playerControllerMP) {
		if (InteractionTweaksModule.INSTANCE.getState() && InteractionTweaksModule.INSTANCE.getMultiTask()) {
			return false;
		}
		
		return playerControllerMP.getIsHittingBlock();
	}*/
	
	@Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;updateController()V"))
	private void updateControllerRedir(PlayerControllerMP playerControllerMP) {
		LazyItemSwitch.INSTANCE.updatePlayerControllerOnTick(playerControllerMP);
	}
}
