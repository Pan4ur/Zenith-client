package me.gopro336.zenith.asm.mixin.mixins.accessor;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShaderGroup.class)
public interface IShaderGroup {

    @Accessor("listShaders")
    List<Shader> getShaderList();

    @Accessor("mainFramebuffer")
    Framebuffer getMainFramebuffer();
}
