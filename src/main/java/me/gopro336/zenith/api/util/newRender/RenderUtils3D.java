package me.gopro336.zenith.api.util.newRender;

import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.asm.mixin.imixin.IRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;

public class RenderUtils3D {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void prepareGL() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
    }

    public static void releaseGL() {
        GL11.glDisable(GL_LINE_SMOOTH);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static AxisAlignedBB generateBB(long x, long y, long z) {
        BlockPos blockPos = new BlockPos(x, y, z);

        return new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + (1) - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }

    public static void draw(BlockPos blockPos, boolean box, boolean outline, double boxHeight, double outlineHeight, Color colour) {
        AxisAlignedBB axisAlignedBB = generateBB(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        prepareGL();

        if (box) {
            drawFilledBox(axisAlignedBB, boxHeight, colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        }

        if (outline) {
            drawBoundingBox(axisAlignedBB, outlineHeight, colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        }

        releaseGL();
    }

    /**
     * Below has been taken and modified from RenderGlobal
     * @see net.minecraft.client.renderer.RenderGlobal
     */

    public static void drawBoundingBox(BufferBuilder bufferBuilder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, 0.0f).endVertex();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder bufferBuilder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
    }

    public static void renderFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

        addChainedFilledBoxVertices(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);

        tessellator.draw();
    }

    public static void renderBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

        drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);

        tessellator.draw();
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB, double height, float red, float green, float blue, float alpha) {
        renderFilledBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, height, red, green, blue, alpha);
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, double height, float red, float green, float blue, float alpha) {
        renderBoundingBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, height, red, green, blue, alpha);
    }

    public static void drawNametagFromBlockPos(BlockPos pos, float height, String text) {
        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + height, pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        FontUtil.drawString(text, 0, 0, -1);
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));

        if (scaleDistance < 1.0f)
            scaleDistance = 1.0f;

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;

        GlStateManager.translate(x - ((IRenderManager)mc.getRenderManager()).getRenderPosX(), y - ((IRenderManager)mc.getRenderManager()).getRenderPosY(), z - ((IRenderManager)mc.getRenderManager()).getRenderPosZ());
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void drawBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, 0.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Prepares for the use of GL, reProperty color and other things, hopefully fixing the grey box issue
     * Idea came from Kami's prepareGL()
     */
    public static void beginRender() {
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.resetColor();
    }

    /**
     * ends the use of GL, to make sure that everything was reset properly
     * Also came from Kami
     */
    public static void endRender() {
        GlStateManager.resetColor();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }
}
