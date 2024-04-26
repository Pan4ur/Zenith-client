package me.gopro336.zenith.event.entity;

import net.minecraft.entity.player.EntityPlayer;

public class TotemPopEvent {
    private EntityPlayer entity;
    private final int popCount;

    public TotemPopEvent(EntityPlayer entity, int count) {
        this.entity = entity;
        this.popCount = count;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }

    public String getName() {
        return entity.getName();
    }

    public int getPopCount() {
        return popCount;
    }

    public int getEntityId() {
        return entity.entityId;
    }
}

