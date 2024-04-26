package me.gopro336.zenith.asm.mixin.mixins.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Entity.class}, priority=0x7FFFFFFF)
public interface IEntity {
    @Invoker(value="setFlag")
    void setFlag(int var1, boolean var2);

    @Accessor(value="inPortal")
    void setInPortal(boolean var1);

    @Accessor(value="isInWeb")
    void setInWeb(boolean var1);
}