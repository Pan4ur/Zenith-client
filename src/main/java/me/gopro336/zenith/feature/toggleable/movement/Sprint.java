package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.event.EventStageable;
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author Gopro336
 */
@AnnotationHelper(name = "Sprint", description = "automaticly sprints for you", category = Category.MOVEMENT)
public class Sprint extends Feature {
	public Property<modes> mode = new Property<>(this, "Mode", "mode to use", modes.Rage);

	public enum modes{
		Rage,
		Legit
	}

	@Listener
	private void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
		if (event.getStage() == EventStageable.EventStage.PRE) {
			if (mode.getValue().equals(modes.Rage)) {
				if (mc.player.movementInput.moveForward != 0 || mc.player.movementInput.moveStrafe != 0) {
					mc.player.setSprinting(true);
				}
			}
			else {
				if (mc.player.movementInput.moveForward >= 0.8F && mc.player.getFoodStats().getFoodLevel() > 6.0f) {
					mc.player.setSprinting(true);
				}
			}
		}
	}
}
