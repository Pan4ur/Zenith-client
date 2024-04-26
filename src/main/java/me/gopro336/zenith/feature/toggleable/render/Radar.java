package me.gopro336.zenith.feature.toggleable.render;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.gopro336.zenith.asm.mixin.mixins.accessor.IEntityRenderer;
import me.gopro336.zenith.api.util.color.RenderUtil;
import me.gopro336.zenith.asm.mixin.imixin.IRenderManager;
import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.jetbrains.annotations.NotNull;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;

/**
 * @author Gopro336
 * Todo: make it not pasted from shitty deobf pyro lol
 */
@AnnotationHelper(name = "Radar", description = "ArrowRadar", category = Category.RENDER)
public class Radar extends Feature {
    public Property<Boolean> players = new Property<>(this, "players", "Players", true);
    public Property<Boolean> friends = new Property<>(this, "friends", "Friends", true);
    public Property<Boolean> bosses = new Property<>(this, "bosses", "Bosses", true);
    public Property<Boolean> hostiles = new Property<>(this, "hostiles", "Hostiles", true);
    public Property<Boolean> passives = new Property<>(this, "passives", "Passives", true);
    public Property<Boolean> items = new Property<>(this, "items", "Items", false);
    public Property<Boolean> other = new Property<>(this, "other", "Other", false);

    public Property<Color> nearColor = new Property<>(this, "NearColor", "", new Color(255, 255, 255, 255));
    public Property<Color> farColor = new Property<>(this, "FarColor", "", new Color(255, 255, 255, 255));

    public NumberProperty<Double> scale = new NumberProperty<>(this, "Scale", "", 0.0D, 1.0D, 10.0D);
    public NumberProperty<Double> distance = new NumberProperty<>(this, "Distance", "", 0.0D, 1.0D, 10.0D);
    public NumberProperty<Double> changeradius = new NumberProperty<>(this, "Color Change Radius", "", 0.0D, 75.0D, 1000.0D);
    public Property<Boolean> hideFrustrum = new Property<>(this, "Hide In Frustrum", "Hide entities you can see", false);
    public NumberProperty<Integer> tilt = new NumberProperty<>(this, "Tilt", "", -90, 50, 90);
    public Property<Boolean> unlockTilt = new Property<>(this, "Unlock Tilt", "Unlock tilt when you look down", false);

    @Listener
    public void onRender3D(Render3DEvent event) {
        block14: {
            if (mc.player == null) break block14;
            Entity entity = mc.getRenderViewEntity();
            if (entity == null) {
                Intrinsics.throwNpe();
            }
            Entity entity2 = mc.getRenderViewEntity();
            if (entity2 == null) {
                Intrinsics.throwNpe();
            }
            Entity entity3 = mc.getRenderViewEntity();
            if (entity3 == null) {
                Intrinsics.throwNpe();
            }
            RenderUtil.camera.setPosition(entity.posX, entity2.posY, entity3.posZ);
            GlStateManager.pushMatrix();
            WorldClient worldClient = ((Minecraft)mc).world;
            if (worldClient == null) {
                Intrinsics.throwNpe();
            }
            for (Entity entity4 : worldClient.loadedEntityList) {
                block16: {
                    block15: {
                        if (entity4 == mc.getRenderViewEntity()) continue;
                        if (hideFrustrum.getValue()) {
                            if (RenderUtil.camera.isBoundingBoxInFrustum(entity4.getEntityBoundingBox())) continue;
                        }
                        if (entity4 instanceof EntityPlayer) {
                            /*if (friends.getValue()) && FriendManager.Companion.isFriend((EntityPlayer)entity4)) {
                                this.c(entity4, this.var0);
                                continue;
                            }*/
                            if (!players.getValue()) continue;
                            this.drawArrow(entity4);
                            continue;
                        }
                        if (entity4 instanceof EntityDragon) break block15;
                        if (!(entity4 instanceof EntityWither)) break block16;
                    }
                    if (!bosses.getValue()) continue;
                    this.drawArrow(entity4);
                    continue;
                }
                /*if (entity4.isCreatureType(EnumCreatureType., false)) {
                    if (!passives.getValue()) continue;
                    this.drawArrow(entity4);
                    continue;
                }*/
                if (entity4.isCreatureType(EnumCreatureType.MONSTER, false)) {
                    if (!hostiles.getValue()) continue;
                    this.drawArrow(entity4);
                    continue;
                }
                if (entity4 instanceof EntityItem) {
                    if (!items.getValue()) continue;
                    this.drawArrow(entity4);
                    continue;
                }
                if (!other.getValue()) continue;
                this.drawArrow(entity4);
            }
            GlStateManager.popMatrix();
        }
    }

    @NotNull
    public Rotation getRotation(@NotNull Vec3d vec3d, @NotNull Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.y - vec3d.y;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = MathHelper.sqrt((d * d + d3 * d3));
        return new Rotation(MathHelper.wrapDegrees(((float)Math.toDegrees(MathHelper.atan2(d3, d)) - 90.0f)), MathHelper.wrapDegrees(((float)(-Math.toDegrees(MathHelper.atan2(d2, d4))))));
    }

    @NotNull
    public Vec3d getEntityVector(@NotNull Entity entity) {
        RenderManager renderManager = mc.getRenderManager();
        double d = this.c(entity.posX, entity.lastTickPosX) - ((IRenderManager)renderManager).getRenderPosX();
        RenderManager renderManager2 = mc.getRenderManager();
        double d2 = this.c(entity.posY, entity.lastTickPosY) - ((IRenderManager)renderManager2).getRenderPosY();
        RenderManager renderManager3 = mc.getRenderManager();
        double d3 = this.c(entity.posZ, entity.lastTickPosZ) - ((IRenderManager)renderManager3).getRenderPosZ();
        return new Vec3d(d, d2, d3);
    }

    public void arrow(float f, float f2, float f3, float f4) {
        GlStateManager.glBegin(6);
        GlStateManager.glVertex3f(f, f2, f3);
        GlStateManager.glVertex3f((f + 0.1f * f4), f2, (f3 - 0.2f * f4));
        GlStateManager.glVertex3f(f, f2, (f3 - 0.12f * f4));
        GlStateManager.glVertex3f((f - 0.1f * f4), f2, (f3 - 0.2f * f4));
        GlStateManager.glEnd();
    }

    //public void c(Entity var1x, Vec3d var2x, Property var3x) {
    public void drawArrow(Entity var1x) {
        if (mc.entityRenderer != null) {
            Color var5x = nearColor.getValue();
            Rotation var8 = getRotation(Vec3d.ZERO, getEntityVector(var1x));
            float var6 = var8.meth2();
            float var7 = var8.getPitch();
            float var16 = (float)180 - var6;
            Entity var10001 = mc.getRenderViewEntity();
            if (var10001 == null) {
                Intrinsics.throwNpe();
            }

            var6 = var16 + var10001.rotationYaw;
            Vec3d var14 = (new Vec3d(0.0D, 0.0D, 1.0D)).rotateYaw((float)Math.toRadians((double)var6)).rotatePitch((float)Math.toRadians(180.0D));
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            float var9 = (float)((double)var1x.getDistance(mc.getRenderViewEntity()) / ((Number)this.changeradius.getValue()).doubleValue());
            //GlStateManager.color(var4x.meth7() * var9 + var5x.meth7() * (1.0F - var9), var4x.g() * var9 + var5x.g() * (1.0F - var9), var4x.meth22() * var9 + var5x.meth22() * (1.0F - var9), var4x.meth9() * var9 + var5x.meth9() * (1.0F - var9));
            GlStateManager.color(var5x.getRed(), var5x.getGreen(), var5x.getBlue(), var5x.getAlpha());
            GlStateManager.disableLighting();
            GlStateManager.loadIdentity();
            EntityRenderer var17 = mc.entityRenderer;
            if (var17 == null) {
                throw new TypeCastException("null cannot be cast to non-null type dev.nuker.pyro.mixin.EntityRendererAccessor");
            } else {
                ((IEntityRenderer)var17).orientCam(mc.getRenderPartialTicks());
                float var10 = (float)((Number)this.scale.getValue()).doubleValue() * 0.2F;
                float var11 = (float)((Number)this.distance.getValue()).doubleValue() * 0.2F;
                var10001 = mc.getRenderViewEntity();
                if (var10001 == null) {
                    Intrinsics.throwNpe();
                }

                GlStateManager.translate(0.0F, var10001.getEyeHeight(), 0.0F);
                Entity var18 = mc.getRenderViewEntity();
                if (var18 == null) {
                    Intrinsics.throwNpe();
                }

                GlStateManager.rotate(-var18.rotationYaw, 0.0F, 1.0F, 0.0F);
                var18 = mc.getRenderViewEntity();
                if (var18 == null) {
                    Intrinsics.throwNpe();
                }

                GlStateManager.rotate(var18.rotationPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, 1.0F);
                float var12 = (float)((Number)this.tilt.getValue()).intValue();
                if (unlockTilt.getValue()) {
                    var16 = (float)90;
                    var10001 = mc.getRenderViewEntity();
                    if (var10001 == null) {
                        Intrinsics.throwNpe();
                    }

                    if (var16 - var10001.rotationPitch < var12) {
                        var16 = (float)90;
                        var10001 = mc.getRenderViewEntity();
                        if (var10001 == null) {
                            Intrinsics.throwNpe();
                        }

                        var12 = var16 - var10001.rotationPitch;
                    }
                }

                GlStateManager.rotate(var12, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(var6, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, var11);
                //var10000 = scale;
                Property var10000 = scale;
                if (var10000 == null) {
                    Intrinsics.throwNpe();
                }

                //float var13 = ((float)var10000.getValue()) * var10 * 2.0f;
                float var13 = Float.valueOf(String.valueOf(var10000.getValue())) * var10 * 2.0f;

                arrow((float)var14.x, (float)var14.y, (float)var14.z, var13);
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableLighting();
            }
        }
    }

    public double c(double d, double d2) {
        return d2 + (d - d2) * (double)mc.getRenderPartialTicks();
    }
}
