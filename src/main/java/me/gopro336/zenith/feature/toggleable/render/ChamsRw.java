package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.api.util.newRender.ColorUtil;
import me.gopro336.zenith.event.render.RenderCrystalEvent;
import me.gopro336.zenith.event.render.RenderEntityModelEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.EntityPreview;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@AnnotationHelper(name = "Chams", description = "", category = Category.RENDER)
public class ChamsRw extends Feature {

    public EntityPreview<EntityPreview.preview> cPreview = new EntityPreview<>(this, "ChamsPreview", "", EntityPreview.preview.Crystal);

    public Property<Mode> mode = new Property<>(this, "Mode", "Mode for Chams", Mode.MODEL);

    //if wire or wiremodel is true
    public NumberProperty<Double> width = new NumberProperty<>(this, "Width", "Line width for the model", 0.0, 3.0, 5.0, v -> mode.getValue().equals(Mode.MODEL)||mode.getValue().equals(Mode.BOTH));

    public Property<Boolean> players = new Property<>(this, "Players", "Renders chams on players", true);
    public Property<Boolean> local = new Property<>(this, "Self", "Renders chams on the local player", false, v -> players.getValue());

    public Property<Boolean> mobs = new Property<>(this, "Mobs", "Renders chams on mobs", true);
    public Property<Boolean> monsters = new Property<>(this, mobs, "Monsters", "Renders chams on monsters", true);

    public Property<Boolean> crystals = new Property<>(this, "Crystals", "Renders chams on crystals", true);
    public NumberProperty<Double> scale = new NumberProperty<>(this, crystals, "Scale", "Scale for crystal model", 0.0, 1.0, 2.0);

    public Property<Boolean> render = new Property<>(this, "Render", "renders chams", true);
    public Property<Boolean> texture = new Property<>(this, render, "Texture", "Enables entity texture", false);
    public Property<Boolean> lighting = new Property<>(this, render, "Lighting", "Disables vanilla lighting", true);
    public Property<Boolean> blend = new Property<>(this, render, "Blend", "Enables blended texture", false);
    public Property<Boolean> transparent = new Property<>(this, render, "Transparent", "Makes entity models transparent", true);
    public Property<Boolean> depth = new Property<>(this, render, "Depth", "Enables entity depth", true);
    public Property<Boolean> walls = new Property<>(this, render, "Walls", "Renders chams models through walls", true);

    public Property<Boolean> highlight = new Property<>(this, render, "Color", "Colors chams models when visible", true, v -> false);
    public Property<Color> highlightColor = new Property<>(this, render, "HighlightColor", "Color of models when visible", new Color(250, 0, 250, 50), v -> highlight.getValue());

    public Property<Boolean> xqz = new Property<>(this, "C", "Colors chams models through walls", true, v -> false);
    public Property<Color> xqzColor = new Property<>(this, "Color", "Color of models through walls", new Color(0, 70, 250, 50));

    @Listener
    public static void onRenderLivingEntity(RenderEntityModelEvent event) {
        /*if (!nullCheck() && (event.getEntity() instanceof EntityOtherPlayerMP && players.getValue() || (event.getEntity() instanceof EntityPlayerSP && local.getValue()) || (isPassiveMob(event.getEntity()) || isNeutralMob(event.getEntity())) && mobs.getValue() || isHostileMob(event.getEntity()) && monsters.getValue())) {
            event.setCanceled(!texture.getValue());

            if (transparent.getValue())
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

            glPushMatrix();
            glPushAttrib(GL_ALL_ATTRIB_BITS);

            if (!texture.getValue() && !mode.getValue().equals(Mode.TEXTURE))
                glDisable(GL_TEXTURE_2D);

            if (blend.getValue())
                glEnable(GL_BLEND);

            if (lighting.getValue())
                glDisable(GL_LIGHTING);

            if (depth.getValue())
                glDepthMask(false);

            if (walls.getValue())
                glDisable(GL_DEPTH_TEST);

            switch (mode.getValue()) {
                case WIREFRAME:
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    break;
                case BOTH:
                case MODEL:
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    break;
            }

            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glLineWidth((float) ((double) width.getValue()));

            if (xqz.getValue())
                ColorUtil.setColor(xqzColor.getValue());

            event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAge(), event.getHeadYaw(), event.getHeadPitch(), event.getScale());

            if (walls.getValue() && !mode.getValue().equals(Mode.BOTH))
                glEnable(GL_DEPTH_TEST);

            if (mode.getValue().equals(Mode.BOTH))
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

            if (highlight.getValue())
                //ColorUtil.setColor(mode.getValue().equals(Mode.BOTH) ? new Color(xqzColor.getValue().getRed(), xqzColor.getValue().getGreen(), xqzColor.getValue().getBlue(), 255) : highlightColor.getValue());
                ColorUtil.setColor(mode.getValue().equals(Mode.BOTH) ? new Color(xqzColor.getValue().getRed(), xqzColor.getValue().getGreen(), xqzColor.getValue().getBlue(), 255) : xqzColor.getValue());

            event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAge(), event.getHeadYaw(), event.getHeadPitch(), event.getScale());

            if (walls.getValue() && mode.getValue().equals(Mode.BOTH))
                glEnable(GL_DEPTH_TEST);

            if (lighting.getValue())
                glEnable(GL_LIGHTING);

            if (depth.getValue())
                glDepthMask(true);

            if (blend.getValue())
                glDisable(GL_BLEND);

            if (!texture.getValue() && !mode.getValue().equals(Mode.TEXTURE))
                glEnable(GL_TEXTURE_2D);

            glPopAttrib();
            glPopMatrix();
        }*/
    }

    @Listener
    public void onRenderCrystalPre(RenderCrystalEvent.RenderCrystalPreEvent event) {
        event.setCanceled(!nullCheck() && isEnabled() && crystals.getValue());
    }

    @Listener
    public void onRenderCrystalPost(RenderCrystalEvent.RenderCrystalPostEvent event) {
        if (!nullCheck() && crystals.getValue()) {
            if (transparent.getValue())
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);

            glPushMatrix();
            glPushAttrib(GL_ALL_ATTRIB_BITS);

            float rotation = event.getEntityEnderCrystal().innerRotation + event.getPartialTicks();
            float rotationMoved = MathHelper.sin(rotation * 0.2F) / 2 + 0.5F;
            rotationMoved += Math.pow(rotationMoved, 2);

            glTranslated(event.getX(), event.getY(), event.getZ());
            glScaled(scale.getValue(), scale.getValue(), scale.getValue());

            if (!texture.getValue() && !mode.getValue().equals(Mode.TEXTURE))
                glDisable(GL_TEXTURE_2D);

            if (blend.getValue())
                glEnable(GL_BLEND);

            if (lighting.getValue())
                glDisable(GL_LIGHTING);

            if (depth.getValue())
                glDepthMask(false);

            if (walls.getValue())
                glDisable(GL_DEPTH_TEST);

            switch (mode.getValue()) {
                case WIREFRAME:
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    break;
                case BOTH:
                case MODEL:
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    break;
            }

            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glLineWidth((float) ((double) width.getValue()));

            if (xqz.getValue())
                ColorUtil.setColor(xqzColor.getValue());

            if (event.getEntityEnderCrystal().shouldShowBottom())
                event.getModelBase().render(event.getEntityEnderCrystal(), 0, rotation * 3, rotationMoved * 0.2F, 0, 0, 0.0625F);
            else
                event.getModelNoBase().render(event.getEntityEnderCrystal(), 0, rotation * 3, rotationMoved * 0.2F, 0, 0, 0.0625F);

            if (walls.getValue() && !mode.getValue().equals(Mode.BOTH))
                glEnable(GL_DEPTH_TEST);

            if (mode.getValue().equals(Mode.BOTH))
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

            if (highlight.getValue())
                //ColorUtil.setColor(mode.getValue().equals(Mode.BOTH) ? new Color(xqzColor.getValue().getRed(), xqzColor.getValue().getGreen(), xqzColor.getValue().getBlue(), 255) : highlightColor.getValue());
                ColorUtil.setColor(mode.getValue().equals(Mode.BOTH) ? new Color(xqzColor.getValue().getRed(), xqzColor.getValue().getGreen(), xqzColor.getValue().getBlue(), 255) : xqzColor.getValue());

            if (event.getEntityEnderCrystal().shouldShowBottom())
                event.getModelBase().render(event.getEntityEnderCrystal(), 0, rotation * 3, rotationMoved * 0.2F, 0, 0, 0.0625F);
            else
                event.getModelNoBase().render(event.getEntityEnderCrystal(), 0, rotation * 3, rotationMoved * 0.2F, 0, 0, 0.0625F);

            if (walls.getValue() && mode.getValue().equals(Mode.BOTH))
                glEnable(GL_DEPTH_TEST);

            if (lighting.getValue())
                glEnable(GL_LIGHTING);

            if (depth.getValue())
                glDepthMask(true);

            if (blend.getValue())
                glDisable(GL_BLEND);

            if (!texture.getValue() && !mode.getValue().equals(Mode.TEXTURE))
                glEnable(GL_TEXTURE_2D);

            glScaled(1 / scale.getValue(), 1 / scale.getValue(), 1 / scale.getValue());

            glPopAttrib();
            glPopMatrix();
        }
    }

    public enum Mode {
        MODEL, WIREFRAME, BOTH, TEXTURE
    }

    public static boolean isPassiveMob(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf) entity).isAngry())
            return false;

        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
            return true;

        return entity instanceof EntityIronGolem && ((EntityIronGolem) entity).getRevengeTarget() == null;
    }

    public static boolean isVehicleMob(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isHostileMob(Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity)) || entity instanceof EntitySpider;
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }
}
