package me.gopro336.zenith.event.world;

public class TickEvent {
    public static TickEvent INSTANCE = new TickEvent();
    public net.minecraftforge.fml.common.gameevent.TickEvent.Phase Field199;

    public net.minecraftforge.fml.common.gameevent.TickEvent.Phase Method324() {
        return this.Field199;
    }

    public static TickEvent Method325(net.minecraftforge.fml.common.gameevent.TickEvent.Phase phase) {
        TickEvent.INSTANCE.Field199 = phase;
        return INSTANCE;
    }
}
