package me.gopro336.zenith.event.entity;

import me.gopro336.zenith.event.EventCancellable;

public class HorseJumpStrengthEvent extends EventCancellable {
	
	private double strength;
	
	public HorseJumpStrengthEvent(double strength) {
		this.strength = strength;
	}
	
	public double getStrength() {
		return strength;
	}
	
	public void setStrength(double strength) {
		this.strength = strength;
	}
}
