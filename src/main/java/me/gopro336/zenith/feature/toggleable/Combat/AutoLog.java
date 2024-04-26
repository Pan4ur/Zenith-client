package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.combat.CrystalUtils;
import me.gopro336.zenith.event.world.UpdateEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.toggleable.misc.AutoReconnect;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@AnnotationHelper(name = "AutoLog", category = Category.COMBAT, description = "Automaticly disconnects from da server if ur ez")
public class AutoLog extends Feature {

    private final Property<Mode> mode = new Property<>(this, "Mode", "", Mode.HEALTH);
    private final NumberProperty<Float> health = new NumberProperty<>(this, "Health", "", 0f, 10f, 22f);
    public NumberProperty<Float> crystalRange = new NumberProperty<>(this, "CrystalRange", "", 1f, 10f, 15f);
    private final Property<Boolean> totem = new Property<>(this, "IgnoreTotem", "", true);
    private Property<Boolean> notify = new Property<>(this, "Notify", "", false);

    private enum Mode {
        HEALTH, PLAYER, CRYSTALDMG
    }

    // implement friend when we do it
    @Listener
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (mode.getValue() == Mode.HEALTH) {
            if (mc.player.getHealth() <= health.getValue()) {
                if (totem.getValue()) disconnect();
                else if (!hasTotems()) disconnect();
            }
        } else {
            if (!totem.getValue() && hasTotems()) return;
            float dmg = 0.0f;

            List<Entity> crystalsInRange = mc.world.loadedEntityList.stream()
                    .filter(e -> e instanceof EntityEnderCrystal)
                    .filter(e -> mc.player.getDistance(e) <= crystalRange.getValue())
                    .collect(Collectors.toList());

            for (Entity entity : crystalsInRange) {
                dmg += CrystalUtils.calculateDamage((EntityEnderCrystal) entity, mc.player);
            }

            if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= dmg) disconnect();
        }
    }

    private boolean hasTotems() {
        for (int slot = 0; slot < 36; slot++) {
            if (mc.player.inventory.getStackInSlot(slot).getItem() == Items.TOTEM_OF_UNDYING) return true;
        }
        return false;
    }

    private void disconnect() {
        AutoReconnect module = (AutoReconnect) FeatureManager.getFeatureByClass(AutoReconnect.class);
        if (module != null && module.isEnabled()) module.toggle();
        this.toggle();
        if (notify.getValue()) {
            Zenith.logger.printToChat("You have AutoLogged!");
        }
        mc.player.inventory.currentItem = 1000;
    }

}
