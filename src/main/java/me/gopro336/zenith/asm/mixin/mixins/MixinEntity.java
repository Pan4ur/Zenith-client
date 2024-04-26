package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.event.entity.AddEntityVelocityEvent;
import me.gopro336.zenith.event.entity.ShouldWalkOffEdgeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Entity.class)
public abstract class MixinEntity{

	@Shadow public double motionY;

	@Shadow public double motionX;

	@Shadow public double motionZ;

	@Shadow protected boolean inPortal;
	
	@Shadow protected abstract void setFlag(int flag, boolean set);

	@Shadow public void move(MoverType type, double x, double y, double z) {}
	
	public void setFlag0(int flag, boolean set) {
		this.setFlag(flag, set);
	}
	
	@Accessor(value = "isInWeb")
	public abstract void setIsInWeb(boolean isInWeb);
	
	@Accessor(value = "inPortal")
	public abstract void setInPortal(boolean inPortal);
	
	@ModifyVariable(method = "addVelocity", at = @At(value = "HEAD"), ordinal = 0)
	private double modifyVariable1(double x) {
		AddEntityVelocityEvent event = new AddEntityVelocityEvent((Entity) (Object) this, x, x, x);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		return event.x;
	}
	
	@ModifyVariable(method = "addVelocity", at = @At(value = "HEAD"), ordinal = 1)
	private double modifyVariable2(double y) {
		AddEntityVelocityEvent event = new AddEntityVelocityEvent((Entity) (Object) this, y, y, y);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		return event.y;
	}
	
	@ModifyVariable(method = "addVelocity", at = @At(value = "HEAD"), ordinal = 2)
	private double modifyVariable3(double z) {
		AddEntityVelocityEvent event = new AddEntityVelocityEvent((Entity) (Object) this, z, z, z);
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		return event.z;
	}
	
	@Redirect(
		method = "move",
		slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;onGround:Z", ordinal = 0)),
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z", ordinal = 0)
	)
	private boolean isSneaking(Entity entity) {
		ShouldWalkOffEdgeEvent event = new ShouldWalkOffEdgeEvent();
		Zenith.INSTANCE.getEventManager().dispatchEvent(event);
		return (event.isCanceled() || entity.isSneaking());
	}
	
	/*@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private void injectGetBrightness(CallbackInfoReturnable<Float> cir) {
		if (VisionModule.INSTANCE.isEnabled() && VisionModule.INSTANCE.getBrightness()) {
			cir.setReturnValue(1f);
			return;
		}
	}*/

	/*@Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
	private void isPushedInject(CallbackInfoReturnable<Boolean> cir) {
		if (VelocityModule.getInstance().isEnabled()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}*/
}
