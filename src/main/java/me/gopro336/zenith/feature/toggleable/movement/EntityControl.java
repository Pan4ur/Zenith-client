package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.asm.mixin.mixins.accessor.IEntityPlayerSP;
import me.gopro336.zenith.event.entity.AbstractHorseSaddledEvent;
import me.gopro336.zenith.event.entity.CanBeSteeredEvent;
import me.gopro336.zenith.event.entity.PigTravelEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "EntityControl", description = "Allows you to control a rideable entity", category = Category.MOVEMENT)
public class EntityControl extends Feature {
	
	public Property<Boolean> ai = new Property<>(this, "PigAI", "", true);
	public Property<Boolean> jump = new Property<>(this, "JumpStrength", "", true);
	
	@Listener
	public void onUpdate() {
		if (jump.getValue()) {
			IEntityPlayerSP player = (IEntityPlayerSP) mc.player;
			player.setHorseJumpPower(1.0f);
		}
	}
	
	@Listener
	private void canBeSteered(CanBeSteeredEvent event) {
		event.setCanceled(true);
	}
	
	@Listener
	private void travel(PigTravelEvent event) {
		if (ai.getValue()) event.setCanceled(true);
	}
	
	@Listener
	private void isSaddled(AbstractHorseSaddledEvent event) {
		event.setCanceled(true);
	}
	
}
