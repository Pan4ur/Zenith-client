package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.asm.mixin.imixin.ITimer;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Robeart
 */
@Mixin(Timer.class)
public abstract class MixinTimer implements ITimer {
	
	@Accessor(value = "tickLength")
	public abstract float getTickLength();
	
	@Accessor(value = "tickLength")
	public abstract void setTickLength(float tickLength);
	
	
}
