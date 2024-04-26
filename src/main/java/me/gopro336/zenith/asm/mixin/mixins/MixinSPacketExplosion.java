package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.asm.mixin.imixin.ISPacketExplosion;
import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Robeart
 */
@Mixin(SPacketExplosion.class)
public abstract class MixinSPacketExplosion implements ISPacketExplosion {
	
	@Accessor(value = "motionX")
	public abstract void setMotionX(float motionX);
	
	@Accessor(value = "motionY")
	public abstract void setMotionY(float motionY);
	
	@Accessor(value = "motionZ")
	public abstract void setMotionZ(float motionZ);
	
}
