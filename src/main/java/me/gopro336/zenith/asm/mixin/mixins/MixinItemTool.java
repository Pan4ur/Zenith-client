package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.asm.mixin.imixin.IItemTool;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Robeart
 */
@Mixin(ItemTool.class)
public abstract class MixinItemTool implements IItemTool {
	
	@Accessor(value = "attackDamage")
	public abstract float getAttackDamage();
	
}
