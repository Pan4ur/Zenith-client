package me.gopro336.zenith.event.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;

public class MoveInputEvent {

    private final MovementInput input;
    private final EntityPlayer player;

    public MoveInputEvent(EntityPlayer player, MovementInput input) {
        this.input = input;
        this.player = player;
    }

    public MovementInput getInput() {
        return input;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}