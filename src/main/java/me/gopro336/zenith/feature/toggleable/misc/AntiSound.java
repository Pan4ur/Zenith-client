package me.gopro336.zenith.feature.toggleable.misc;

import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author Gopro336
 */

@AnnotationHelper(name = "AntiSound", description = "", category = Category.MISC)
public class AntiSound extends Feature {

    public final Property<Boolean> wither = new Property<>(this, "Wither Ambient", "", true);
    private final Property<Boolean> witherHurt = new Property<>(this, "Wither Hurt", "",true);
    public final Property<Boolean> witherSpawn = new Property<>(this, "Wither Spawn", "", false);
    private final Property<Boolean> witherDeath = new Property<>(this, "Wither Death", "", false);
    private final Property<Boolean> punches = new Property<>(this, "Punches", "", true);
    private final Property<Boolean> punchW = new Property<>(this, "Weak Punch", "", true);
    private final Property<Boolean> punchKB = new Property<>(this, "Knockback Punch", "", true);
    private final Property<Boolean> explosion = new Property<>(this, "Explosion", "", false);
    public final Property<Boolean> totem = new Property<>(this, "Totem Pop", "", false);
    public final Property<Boolean> elytra = new Property<>(this, "Elytra Wind", "", true);
    public final Property<Boolean> portal = new Property<>(this, "Nether Portal", "", true);

    @Listener
    public void onRecieve(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (shouldCancelSound(packet.getSound())) {
                event.setCanceled(true);
            }
        }
    }

    private boolean shouldCancelSound(SoundEvent soundEvent) {
        if (soundEvent == SoundEvents.ENTITY_WITHER_AMBIENT && wither.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_WITHER_SPAWN && witherSpawn.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_WITHER_HURT && witherHurt.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_WITHER_DEATH && witherDeath.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE && punches.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_WEAK && punchW.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK && punchKB.getValue()) {
            return true;
        } else if (soundEvent == SoundEvents.ENTITY_GENERIC_EXPLODE && explosion.getValue()) {
            return true;
        }
        return false;
    }
}
