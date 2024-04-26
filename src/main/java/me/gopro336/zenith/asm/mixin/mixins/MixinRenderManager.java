package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.event.EventStageable;
import me.gopro336.zenith.event.render.RenderEntityEvent;
import me.gopro336.zenith.asm.mixin.imixin.IRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IRenderManager {
	
	@Accessor(value = "renderPosX")
	public abstract double getRenderPosX();
	
	@Accessor(value = "renderPosY")
	public abstract double getRenderPosY();
	
	@Accessor(value = "renderPosZ")
	public abstract double getRenderPosZ();
	
	@Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
	public void renderEntityPre(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
		RenderEntityEvent event = new RenderEntityEvent(EventStageable.EventStage.PRE, entityIn, x, y, z, yaw, partialTicks);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		if (event.isCanceled()) ci.cancel();
	}
	
	@Inject(method = "renderEntity", at = @At("TAIL"), cancellable = true)
	public void renderEntityPost(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
		RenderEntityEvent event = new RenderEntityEvent(EventStageable.EventStage.POST, entityIn, x, y, z, yaw, partialTicks);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
	}
	
}
