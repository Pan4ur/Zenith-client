package me.gopro336.zenith.feature.toggleable.movement;

import com.mojang.authlib.GameProfile;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.jetbrains.annotations.Nullable;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@AnnotationHelper(name = "Blink", category = Category.MOVEMENT, description = "Cancels all player packets")
public class Blink extends Feature {

    public Property<Boolean> playerModel = new Property<>(this, "PlayerModel", "", true);

    Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        if (playerModel.getValue())
            createFakePlayer(null, true, true, true, true, 6640);

        ChatUtils.message("Cancelling all player packets!");
    }

    public static void createFakePlayer(@Nullable String name, boolean copyInventory, boolean copyAngles, boolean health, boolean player, int entityID) {
        EntityOtherPlayerMP entity = player ? new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile()) : new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), name));
        entity.copyLocationAndAnglesFrom(mc.player);

        if (copyInventory)
            entity.inventory.copyInventory(mc.player.inventory);

        if (copyAngles) {
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
        }

        if (health)
            entity.setHealth(mc.player.getHealth() + mc.player.getAbsorptionAmount());

        mc.world.addEntityToWorld(entityID, entity);
    }

    @Override
    public void onDisable() {
        if (nullCheck())
            return;

        mc.world.removeEntityFromWorld(69420);

        for (Packet<? extends INetHandler> packet : packets) {
            mc.player.connection.sendPacket(packet);
        }

        packets.clear();
    }

    @Listener
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketTabComplete || event.getPacket() instanceof CPacketClientStatus)
            return;

        packets.add(event.getPacket());
        event.setCanceled(true);
    }
}
