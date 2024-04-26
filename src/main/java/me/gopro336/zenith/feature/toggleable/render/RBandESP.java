package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@AnnotationHelper(name = "RBandESP", description = "renders ruberbands", category = Category.RENDER)
public class RBandESP extends Feature {

    public NumberProperty<Integer> fadeTime = new NumberProperty<>(this, "FadeTime", "time for rendered objects to fade", 1, 3000, 5000);

    public NumberProperty<Float> lineWidth = new NumberProperty<>(this, "LineWidth", "line width for render", 0.1f, 1f, 3f);

    public Property<Color> color = new Property<>(this, "Color", "render color", new Color(0,255, 255, 255));


    private final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > (long) fadeTime.getValue()) {
                popFakePlayerMap.remove(entry.getKey());
                continue;
            }
            GL11.glPushMatrix();
            GL11.glDepthRange(0.0, 0.01);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidth.getValue());
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            this.glColor();
            renderEntity(entry.getKey(), event.getPartialTicks(), false);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthRange(0.0, 1.0);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    @Listener
    public void onPacket(PacketReceiveEvent event) {
        for(EntityPlayer e : mc.world.playerEntities) {
            if(event.getPacket() instanceof SPacketPlayerPosLook) {
                if (mc.world.getEntityByID(e.getEntityId()) != null) {
                    final Entity entity = mc.world.getEntityByID(e.getEntityId());
                    if (entity instanceof EntityPlayer) {
                        final EntityPlayer player = (EntityPlayer) entity;
                        final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
                        fakeEntity.copyLocationAndAnglesFrom(player);
                        fakeEntity.rotationYawHead = player.rotationYawHead;
                        fakeEntity.prevRotationYawHead = player.rotationYawHead;
                        fakeEntity.rotationYaw = player.rotationYaw;
                        fakeEntity.prevRotationYaw = player.rotationYaw;
                        fakeEntity.rotationPitch = player.rotationPitch;
                        fakeEntity.prevRotationPitch = player.rotationPitch;
                        fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                        fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                        popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    private void glColor() {
        final Color clr = color.getValue();
        GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, color.getValue().getAlpha() / 255f);
    }

    public void renderEntity(Entity entityIn, float partialTicks, boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }

        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();

        if (entityIn.isBurning()) {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
    }
}
