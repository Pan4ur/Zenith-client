package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.render.*;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;

/**
 * todo: complete thsi module
 */
@AnnotationHelper(name = "NoRender", category = Category.RENDER, description = "Prevents certain things from rendering")
public class NoRender extends Feature {
    public static NoRender INSTANCE;

    public Property<Boolean> overlays = new Property<>(this, "Overlays", "Prevents overlays from rendering", true);
    public Property<Boolean> overlayFire = new Property<>(this, "Fire", "Prevents fire overlay from rendering", true);
    public Property<Boolean> overlayLiquid = new Property<>(this, "Liquid", "Prevents liquid overlay from rendering", true);
    public Property<Boolean> overlayBlock = new Property<>(this, "Block", "Prevents block overlay from rendering", true);
    public Property<Boolean> overlayBoss = new Property<>(this, "Boss", "Prevents boss bar overlay from rendering", true);

    public Property<Boolean> fog = new Property<>(this, "Fog", "Prevents fog from rendering", true);
    public Property<Boolean> fogLiquid = new Property<>(this, "LiquidVision", "Clears fog in liquid", true);
    public NumberProperty<Double> fogDensity = new NumberProperty<>(this, "Density", "Density of the fog", 0.0d, 0.0d, 20.0d);

    public Property<Boolean> armor = new Property<>(this, "Armor", "Prevents armor from rendering", true);
    public Property<Boolean> items = new Property<>(this, "Items", "Prevents dropped items from rendering", true);
    public Property<Boolean> particles = new Property<>(this, "Particles", "Prevents laggy particles from rendering", true);
    public Property<Boolean> tileEntities = new Property<>(this, "TileEntities", "Prevents tile entity effects (enchantment table books, beacon beams, etc.) from rendering", false);
    public Property<Boolean> maps = new Property<>(this, "Maps", "Prevents maps from rendering", true);
    public Property<Boolean> skylight = new Property<>(this, "Skylight", "Prevents skylight updates from rendering", true);
    public Property<Boolean> hurtCamera = new Property<>(this, "HurtCamera", "Removes the hurt camera effect", true);
    public Property<Boolean> witherSkull = new Property<>(this, "WitherSkull", "Prevents flying wither skulls from rendering", true);
    public Property<Boolean> potion = new Property<>(this, "Potion", "Removes certain potion effects", true);
    public Property<Boolean> fov = new Property<>(this, "FOV", "Removes the FOV modifier effect", true);

    @Override
    public void onUpdate() {
        if (items.getValue()) {
            for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                if (entity instanceof EntityItem)
                    mc.world.removeEntity(entity);
            }
        }

        if (potion.getValue()) {
            if (mc.player.isPotionActive(MobEffects.BLINDNESS))
                mc.player.removePotionEffect(MobEffects.BLINDNESS);

            if (mc.player.isPotionActive(MobEffects.NAUSEA))
                mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
    
    @Listener
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (nullCheck() && overlays.getValue()) {
            if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.FIRE) && overlayFire.getValue())
                event.setCanceled(true);

            if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.WATER) && overlayLiquid.getValue())
                event.setCanceled(true);

            if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.BLOCK) && overlayBlock.getValue())
                event.setCanceled(true);
        }
    }

    @Listener
    public void onRenderBossOverlay(BossOverlayEvent event) {
        event.setCanceled(nullCheck() && overlayBoss.getValue());
    }

    @Listener
    public void onRenderEnchantmentTableBook(RenderEnchantmentTableBookEvent event) {
        event.setCanceled(nullCheck() && tileEntities.getValue());
    }

    @Listener
    public void onRenderBeaconBeam(RenderBeaconBeamEvent event) {
        event.setCanceled(nullCheck() && tileEntities.getValue());
    }

    @Listener
    public void onRenderSkylight(RenderSkylightEvent event) {
        event.setCanceled(nullCheck() && skylight.getValue());
    }

    @Listener
    public void onRenderMap(RenderMapEvent event) {
        event.setCanceled(nullCheck() && maps.getValue());
    }

    @Listener
    public void onLayerArmor(LayerArmorEvent event) {
        if (nullCheck() && armor.getValue()) {
            event.setCanceled(true);
            switch (event.getEntityEquipmentSlot()) {
                case HEAD:
                    event.getModelBiped().bipedHead.showModel = false;
                    event.getModelBiped().bipedHeadwear.showModel = false;
                    break;
                case CHEST:
                    event.getModelBiped().bipedBody.showModel = false;
                    event.getModelBiped().bipedRightArm.showModel = false;
                    event.getModelBiped().bipedLeftArm.showModel = false;
                    break;
                case LEGS:
                    event.getModelBiped().bipedBody.showModel = false;
                    event.getModelBiped().bipedRightLeg.showModel = false;
                    event.getModelBiped().bipedLeftLeg.showModel = false;
                    break;
                case FEET:
                    event.getModelBiped().bipedRightLeg.showModel = false;
                    event.getModelBiped().bipedLeftLeg.showModel = false;
                    break;
                case MAINHAND:
                case OFFHAND:
                	break;
            }
        }
    }

    @Listener
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketParticles && ((SPacketParticles) event.getPacket()).getParticleCount() > 200)
            event.setCanceled(true);
    }

    @Listener
    public void onHurtCamera(HurtCameraEffectEvent event) {
        event.setCanceled(nullCheck() && hurtCamera.getValue());
    }

    @Listener
    public void onRenderWitherSkull(RenderWitherSkullEvent event) {
        event.setCanceled(nullCheck() && witherSkull.getValue());
    }
    
    @Listener
    public void onRenderFog(EntityViewRenderEvent.FogDensity event) {
        if (nullCheck() && fog.getValue()) {
            if (!isInLiquid() && fogLiquid.getValue())
                return;

            event.setDensity((float) ((double) fogDensity.getValue()));
            event.setCanceled(true);
        }
    }

    @Listener
    public void onFOVModifier(ModifyFOVEvent event) {
        event.setCanceled(nullCheck() && fov.getValue());
    }

    public static boolean isInLiquid() {
        return mc.player.isInLava() || mc.player.isInWater();
    }
}
