package me.gopro336.zenith.asm.mixin.imixin;

import net.minecraft.util.EnumFacing;

/**
 * @author Robeart 25/07/2020
 */
public interface ICPacketPlayerTryUseItemOnBlock {

    void setDirection(EnumFacing placedBlockDirection);

}
