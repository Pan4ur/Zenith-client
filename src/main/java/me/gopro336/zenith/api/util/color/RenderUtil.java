package me.gopro336.zenith.api.util.color;

import me.gopro336.zenith.api.util.IGlobals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;

import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

//skid

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;


//skid
@Deprecated
public class RenderUtil extends Tessellator implements IGlobals
{
	public static ICamera camera = new Frustum();
	public static RenderUtil INSTANCE = new RenderUtil();

	/**
	 * @skidder Gopro336
	 *
	 */
	public RenderUtil() {
		super(0x200000);
	}

	public RenderUtil(int bufferSize) {
		super(bufferSize);
	}

	public static void prepare(int mode) {
		prepareGL();
		begin(mode);
	}

	public static void releaseGL() {
		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableDepth();
	}

	public static void release() {
		render();
		releaseGL();
	}

	public static void render() {
		INSTANCE.draw();
	}

	public static void begin(int mode) {
		INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
	}

	public static void prepareGL() {
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(1.5F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.color(1,1,1);
	}

	public static void drawBlurBox(final int X, final int Y, final int width, final int height) {
		glEnable(GL_STENCIL_TEST);
		GL11.glScissor(X, Y, width,height);
		if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
			if (mc.entityRenderer.getShaderGroup() != null) {
				mc.entityRenderer.getShaderGroup().deleteShaderGroup();
			}
			mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}
		glDisable(GL_SCISSOR_TEST);
	}
	public static void drawBorderedCircle(final float circleX, final float circleY, final double radius, final double width, final int borderColor, final int innerColor) {
		enableGL2D();
		GL11.glBegin(4);
		GlStateManager.enableBlend();
		GL11.glEnable(2881);
		drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
		drawFullCircle(circleX, circleY, (float)radius, innerColor);
		GlStateManager.disableBlend();
		GL11.glDisable(2881);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		disableGL2D();
	}

	public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
		//  GL11.glEnable((int)3042);
		GL11.glDisable((int)3553);
		GL11.glBlendFunc((int)770, (int)771);
		GL11.glEnable((int)2848);
		GL11.glPushMatrix();
		float scale = 0.1f;
		GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
		drawCircle(x *= 10, y *= 10, radius *= 10.0f, insideC);
		// drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
		GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
		GL11.glPopMatrix();
		GL11.glEnable((int)3553);
		//  GL11.glDisable((int)3042);
		GL11.glDisable((int)2848);
	}

	public static void drawCircle(double x, double y, float radius, int color) {
		float alpha = (float)(color >> 24 & 255) / 255.0f;
		float red = (float)(color >> 16 & 255) / 255.0f;
		float green = (float)(color >> 8 & 255) / 255.0f;
		float blue = (float)(color & 255) / 255.0f;
		GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
		GL11.glBegin((int)9);
		int i = 0;
		while (i <= 360) {
			GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
			++i;
		}
		GL11.glEnd();
	}

	public static void drawFullCircle(float cx, float cy, float r, final int c) {
		r *= 2.0f;
		cx *= 2.0f;
		cy *= 2.0f;
		final float theta = 0.19634953f;
		final float p = (float)Math.cos(theta);
		final float s = (float)Math.sin(theta);
		float x = r;
		float y = 0.0f;
		enableGL2D();
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glEnable(3024);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		glColor(c);
		GL11.glBegin(9);
		for (int ii = 0; ii < 32; ++ii) {
			GL11.glVertex2f(x + cx, y + cy);
			float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		disableGL2D();
	}

	public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
		GL11.glPushMatrix();
		cx *= 2.0f;
		cy *= 2.0f;
		final float f = (c >> 24 & 0xFF) / 255.0f;
		final float f2 = (c >> 16 & 0xFF) / 255.0f;
		final float f3 = (c >> 8 & 0xFF) / 255.0f;
		final float f4 = (c & 0xFF) / 255.0f;
		final float theta = (float)(6.2831852 / num_segments);
		final float p = (float)Math.cos(theta);
		final float s = (float)Math.sin(theta);
		float x;
		r = (x = r * 2.0f);
		float y = 0.0f;
		enableGL2D();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(2);
		for (int ii = 0; ii < num_segments; ++ii) {
			GL11.glVertex2f(x + cx, y + cy);
			final float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		disableGL2D();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
	}

	public static void glColor(final int hex) {
		final float alpha = (hex >> 24 & 0xFF) / 255.0f;
		final float red = (hex >> 16 & 0xFF) / 255.0f;
		final float green = (hex >> 8 & 0xFF) / 255.0f;
		final float blue = (hex & 0xFF) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
		final float red = 0.003921569f * redRGB;
		final float green = 0.003921569f * greenRGB;
		final float blue = 0.003921569f * blueRGB;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		//GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}


	public static void drawGradientRect(final float x, final float y, final float w, final float h, final int startColor, final int endColor) {
		final float f = (startColor >> 24 & 0xFF) / 255.0f;
		final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
		final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
		final float f4 = (startColor & 0xFF) / 255.0f;
		final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
		final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
		final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
		final float f8 = (endColor & 0xFF) / 255.0f;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(x + (double)w, (double)y, 0.0).color(f2, f3, f4, f).endVertex();
		vertexbuffer.pos((double)x, (double)y, 0.0).color(f2, f3, f4, f).endVertex();
		vertexbuffer.pos((double)x, y + (double)h, 0.0).color(f6, f7, f8, f5).endVertex();
		vertexbuffer.pos(x + (double)w, y + (double)h, 0.0).color(f6, f7, f8, f5).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawGradientBlockOutline(final BlockPos pos, final Color startColor, final Color endColor, final float linewidth) {
		final IBlockState iblockstate = Minecraft.getMinecraft().world.getBlockState(pos);
		final Vec3d interp = interpolateEntity((Entity)Minecraft.getMinecraft().player, Minecraft.getMinecraft().getRenderPartialTicks());
		drawAGradientBlockOutline(iblockstate.getSelectedBoundingBox((World)Minecraft.getMinecraft().world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), startColor, endColor, linewidth);
	}
	

	public static void drawProperGradientBlockOutline(final BlockPos pos, final Color startColor, final Color midColor, final Color endColor, final float linewidth) {
		final IBlockState iblockstate = Minecraft.getMinecraft().world.getBlockState(pos);
		final Vec3d interp = interpolateEntity((Entity)Minecraft.getMinecraft().player, Minecraft.getMinecraft().getRenderPartialTicks());
		drawProperGradientBlockOutline(iblockstate.getSelectedBoundingBox((World)Minecraft.getMinecraft().world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), startColor, midColor, endColor, linewidth);
	}

	public static void drawProperGradientBlockOutline(final AxisAlignedBB bb, final Color startColor, final Color midColor, final Color endColor, final float linewidth) {
		final float red = endColor.getRed() / 255.0f;
		final float green = endColor.getGreen() / 255.0f;
		final float blue = endColor.getBlue() / 255.0f;
		final float alpha = endColor.getAlpha() / 255.0f;
		final float red2 = midColor.getRed() / 255.0f;
		final float green2 = midColor.getGreen() / 255.0f;
		final float blue2 = midColor.getBlue() / 255.0f;
		final float alpha2 = midColor.getAlpha() / 255.0f;
		final float red3 = startColor.getRed() / 255.0f;
		final float green3 = startColor.getGreen() / 255.0f;
		final float blue3 = startColor.getBlue() / 255.0f;
		final float alpha3 = startColor.getAlpha() / 255.0f;
		final double dif = (bb.maxY - bb.minY) / 2.0;
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glLineWidth(linewidth);
		GL11.glBegin(1);
		GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
		GL11.glVertex3d(bb.minX, bb.minY + dif, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY + dif, bb.minZ);
		GL11.glColor4f(red3, green3, blue3, alpha3);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
		GL11.glVertex3d(bb.minX, bb.minY + dif, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY + dif, bb.maxZ);
		GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
		GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.maxZ);
		GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
		GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.minZ);
		GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glEnd();
		GL11.glDisable(2848);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawAGradientBlockOutline(final AxisAlignedBB bb, final Color startColor, final Color endColor, final float linewidth) {
		final float red = startColor.getRed() / 255.0f;
		final float green = startColor.getGreen() / 255.0f;
		final float blue = startColor.getBlue() / 255.0f;
		final float alpha = startColor.getAlpha() / 255.0f;
		final float red2 = endColor.getRed() / 255.0f;
		final float green2 = endColor.getGreen() / 255.0f;
		final float blue2 = endColor.getBlue() / 255.0f;
		final float alpha2 = endColor.getAlpha() / 255.0f;
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glLineWidth(linewidth);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GL11.glDisable(2848);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawGradientFilledBox(final BlockPos pos, final Color startColor, final Color endColor) {
		final IBlockState iblockstate = Minecraft.getMinecraft().world.getBlockState(pos);
		final Vec3d interp = interpolateEntity((Entity)Minecraft.getMinecraft().player, Minecraft.getMinecraft().getRenderPartialTicks());
		drawAGradientFilledBox(iblockstate.getSelectedBoundingBox((World)Minecraft.getMinecraft().world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), startColor, endColor);
	}

	public static Vec3d interpolateEntity(final Entity entity, final float time) {
		return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
	}

	public static void drawAGradientFilledBox(final AxisAlignedBB bb, final Color startColor, final Color endColor) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		final float alpha = endColor.getAlpha() / 255.0f;
		final float red = endColor.getRed() / 255.0f;
		final float green = endColor.getGreen() / 255.0f;
		final float blue = endColor.getBlue() / 255.0f;
		final float alpha2 = startColor.getAlpha() / 255.0f;
		final float red2 = startColor.getRed() / 255.0f;
		final float green2 = startColor.getGreen() / 255.0f;
		final float blue2 = startColor.getBlue() / 255.0f;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawRectDouble(double left, double top, double right, double bottom, int color)
	{
		if (left < right)
		{
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			double j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(left, bottom, 0.0D).endVertex();
		bufferbuilder.pos(right, bottom, 0.0D).endVertex();
		bufferbuilder.pos(right, top, 0.0D).endVertex();
		bufferbuilder.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRectOutline(double left, double top, double right, double bottom, double width, int color) {
		drawRectOutline(left - width, top - width, right + width, bottom + width, left, top, right, bottom, color);
	}

	public static void drawRectOutline(double left, double top, double right, double bottom, double left2, double top2, double right2, double bottom2, int color) {
		//First 4 are the bigger box of the outline
		//Second 4 are the cutout box (should be the original box coordinates)
		drawRectDouble(left, top, right, top2, color);
		drawRectDouble(right2, top2, right, bottom, color);
		drawRectDouble(left, bottom2, right2, bottom, color);
		drawRectDouble(left, top2, left2, bottom2, color);
	}

	/*public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
		if (box) {
			RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
		}
		if (outline) {
			RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
		}
	}

	public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
		if (box) {
			RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
		}
		if (outline) {
			RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, height, gradientOutline, invertGradientOutline, gradientAlpha);
		}
	}

	public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
		AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
		camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
		if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.disableDepth();
			GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glLineWidth(lineWidth);
			double dist = RenderUtil.mc.player.getDistance((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f) * 0.75;
			if (box) {
				RenderGlobal.renderFilledBox(bb, (float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) boxAlpha / 255.0f);
			}
			if (outline) {
				RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
			}
			GL11.glDisable(2848);
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	public static void drawBox(AxisAlignedBB box, float r, float g, float b, float a)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glLineWidth(1.5f);

		RenderGlobal.renderFilledBox(box, r, g, b, a);
		RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5F);

		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBoxFromBlockpos(BlockPos blockPos, float r, float g, float b, float a)
	{
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ, blockPos.getX() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
		drawBox(axisAlignedBB, r, g, b, a);
	}*/
}
