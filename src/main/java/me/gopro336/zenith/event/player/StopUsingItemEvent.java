package me.gopro336.zenith.event.player;

import me.gopro336.zenith.event.EventCancellable;

public class StopUsingItemEvent extends EventCancellable {
    private boolean packet = false;

    public boolean isPacket() {
        return packet;
    }

    public void setPacket(boolean packet) {
        this.packet = packet;
    }
}