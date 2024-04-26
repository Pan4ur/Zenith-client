package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.api.util.client.PlayerUtils;
import me.gopro336.zenith.event.player.MoveEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "FastSwim", category = Category.MOVEMENT)
public class FastSwim extends Feature {
    private NumberProperty<Double> speed = new NumberProperty<>(this, "Speed", "", 0.1D, 2D, 10D);
    private Property<Boolean> antiKick = new Property<>(this, "AntiKick", "", true);

    @Listener
    public void onPlayerMove(MoveEvent event) {
        if (!mc.player.isInWater() || !PlayerUtils.isPlayerMoving()) return;
        double[] dir;
        if (mc.player.ticksExisted % 4 == 0 && antiKick.getValue()) {
            dir = PlayerUtils.directionSpeed(speed.getValue()/40);
        } else {
            dir = PlayerUtils.directionSpeed(speed.getValue()/10);
        }
        event.setX(dir[0]);
        event.setZ(dir[1]);
    }
}
