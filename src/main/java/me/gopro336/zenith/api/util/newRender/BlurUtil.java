package me.gopro336.zenith.api.util.newRender;

import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.asm.mixin.mixins.accessor.IShaderGroup;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil implements IGlobals {

    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static final ResourceLocation BLUR_LOCATION = new ResourceLocation("zenith/shader/blur.json");

    public static void initFboAndShader() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), BLUR_LOCATION);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = ((IShaderGroup) blurShader).getMainFramebuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
        ((IShaderGroup) blurShader).getShaderList().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        ((IShaderGroup) blurShader).getShaderList().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);

        ((IShaderGroup) blurShader).getShaderList().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        ((IShaderGroup) blurShader).getShaderList().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurArea(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution sr = new ScaledResolution(mc);

        int factor = sr.getScaleFactor();
        int factor2 = sr.getScaledWidth();
        int factor3 = sr.getScaledHeight();

        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        if (OpenGlHelper.isFramebufferEnabled()) {

          //  buffer.framebufferClear();

            GL11.glScissor(
                    x * factor,
                    (mc.displayHeight - (y * factor) - height * factor),
                    width * factor,
                    height * factor - 12
            );

            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            setShaderConfigs(intensity, blurWidth, blurHeight);
            buffer.bindFramebuffer(true);
            blurShader.render(mc.getRenderPartialTicks());

            mc.getFramebuffer().bindFramebuffer(true);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateUtils.blend(true);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
            buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
            GlStateUtils.blend(false);
            GL11.glScalef(factor, factor, 0);

        }
    }

    public static void blurArea(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;


        buffer.framebufferClear();

        GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor,
                (height) * factor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        setShaderConfigs(intensity, 1, 0);
        buffer.bindFramebuffer(true);
        blurShader.render(mc.getRenderPartialTicks());

        mc.getFramebuffer().bindFramebuffer(true);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
        GlStateManager.disableBlend();
        GL11.glScalef(factor, factor, 0);
        RenderHelper.enableGUIStandardItemLighting();

    }

    public static void blurAreaGey(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        buffer.framebufferClear();

        GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor,
                (height) * factor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        setShaderConfigs(intensity, 1, 0);
        buffer.bindFramebuffer(true);
        blurShader.render(mc.getRenderPartialTicks());

        mc.getFramebuffer().bindFramebuffer(true);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
        GlStateManager.disableBlend();
        GL11.glScalef(factor, factor, 0);
        RenderHelper.enableGUIStandardItemLighting();
    }

    public static void blurAreaBoarder(float x, float f, float width, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        GL11.glScissor((int)(x * factor), (int)((mc.displayHeight - (f * factor) - height * factor)) +1, (int)(width * factor),
                (int)(height * factor));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

			/*Stencil.write(false);
			rect
			Stencil.erase(true);*/

        setShaderConfigs(intensity, 1, 0);
        buffer.bindFramebuffer(true);

        blurShader.render(mc.getRenderPartialTicks());

        mc.getFramebuffer().bindFramebuffer(true);

        //Stencil.dispose();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void blurShape(float g, float f, float h, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        GL11.glScissor((int)(g * factor), (int)((mc.displayHeight - (f * factor) - height * factor)) +1, (int)(h * factor),
                (int)(height * factor));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        setShaderConfigs(intensity, 1, 0);
        buffer.bindFramebuffer(true);

        blurShader.render(mc.getRenderPartialTicks());

        mc.getFramebuffer().bindFramebuffer(true);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //GlStateManager.enableBlend();
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        int factor2 = sr.getScaledWidth();
        int factor3 = sr.getScaledHeight();

        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            initFboAndShader();
        }

        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        if (OpenGlHelper.isFramebufferEnabled()) {

            GL11.glScissor(x * factor,
                    (mc.displayHeight - (y * factor) - height * factor),
                    width * factor,
                    height * factor);

            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            setShaderConfigs(intensity, blurWidth, blurHeight);

            buffer.bindFramebuffer(true);

            blurShader.render(mc.getRenderPartialTicks());

            mc.getFramebuffer().bindFramebuffer(true);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateUtils.matrix(true);
            GlStateUtils.blend(true);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);

            buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);

            GlStateUtils.blend(false);
            GL11.glScalef(factor, factor, 0);
            RenderHelper.enableGUIStandardItemLighting();
            GlStateUtils.matrix(false);

        }

    }

    public static void blurAll(float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        setShaderConfigs(intensity, 1, 0);
        buffer.bindFramebuffer(true);
        blurShader.render(mc.getRenderPartialTicks());

        mc.getFramebuffer().bindFramebuffer(true);

    }

    public static void boxBlurArea(int x, int y, int width, int height) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
//        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
//                || blurShader == null) {
//            initFboAndShader();
//        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor,
                (height) * factor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.disableBlend();
        GL11.glScalef(factor, factor, 0);
        RenderHelper.enableGUIStandardItemLighting();

    }

}