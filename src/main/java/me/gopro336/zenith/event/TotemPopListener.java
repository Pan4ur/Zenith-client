package me.gopro336.zenith.event;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.event.entity.TotemPopEvent;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.HashMap;
import java.util.Map;

public class TotemPopListener implements IGlobals {

    public final Map<String, Integer> popMap = new HashMap<>();
    private final Zenith notorious = Zenith.INSTANCE;

    public TotemPopListener() {
        Zenith.INSTANCE.getEventManager().addEventListener(this);
    }

    @Listener
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                final Entity entity = packet.getEntity(mc.world);
                if (entity instanceof EntityPlayer) {
                    if (entity.equals(mc.player))
                        return;

                    final EntityPlayer player = (EntityPlayer) entity;
                    handlePop(player);
                }
            }
        }
    }

    public void handlePop(EntityPlayer player) {
        if (!popMap.containsKey(player.getName())) {
            Zenith.INSTANCE.getEventManager().dispatchEvent(new TotemPopEvent(player, 1));
            popMap.put(player.getName(), 1);
        } else {
            popMap.put(player.getName(), popMap.get(player.getName()) + 1);
            Zenith.INSTANCE.getEventManager().dispatchEvent(new TotemPopEvent(player, popMap.get(player.getName())));
        }
    }

    /*@Listener
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == FakePlayer2.INSTANCE.fakePlayer)
                continue;

            if (player != mc.player && popMap.containsKey(player.getName())) {
                if ((player.isDead || !player.isEntityAlive() || player.getHealth() <= 0)) {
                    PopCounter.INSTANCE.onDeath(player.getName(), popMap.get(player.getName()), player.getEntityId());
                    popMap.remove(player.getName());
                }
            }
        }
    }*/
}