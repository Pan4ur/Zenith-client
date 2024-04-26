package me.gopro336.zenith.asm.mixin.imixin;

import net.minecraft.util.Timer;

/**
 * @author Robeart
 */
public interface IMinecraft {
	
	int getRightClickDelayTimer();
	
	void setRightClickDelayTimer(int rightClickDelayTimer);
	
	Timer getTimer();
	
}
