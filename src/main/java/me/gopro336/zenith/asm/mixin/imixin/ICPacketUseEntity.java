package me.gopro336.zenith.asm.mixin.imixin;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntity {

    @Accessor("entityID")
    void setID(int id);

    @Accessor("action")
    void setAction(Action action);
}
