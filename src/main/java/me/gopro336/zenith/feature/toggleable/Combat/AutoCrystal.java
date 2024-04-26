package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;

/**
 * @author Gopro336
 * Todo: actually make it
 */
@AnnotationHelper(name = "AutoCrystal", description = "Blows ppl wit crystals", category = Category.COMBAT)
public class AutoCrystal extends Feature {

    public static Property<Boolean> terrainIgnore;
    public static NumberProperty<Integer> predictTicks;
    public static Property<Boolean> collision;

    public AutoCrystal() {
        terrainIgnore = new Property<>(this, "PredictDestruction", "", false);//.withParent(prediction).withDescription("Ignore destructable blocks when doing damage calculations");
        predictTicks = new NumberProperty<>(this, "PredictTicks", "", 0, 1, 10);//.withParent(prediction).withDescription("Predict target motion by this amount of ticks");
        collision = new Property<>(this, "Collision", "", false);//.withParent(prediction).withDescription("Simulate collision when predicting motion");
    }
}