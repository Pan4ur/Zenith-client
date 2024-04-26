package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;

@AnnotationHelper(name = "Rainbow", category = Category.CLIENT)
public class RainbowFeature
extends Feature {
    /*public static Value<Integer> Saturation;
    public static Value<Integer> Brightness;
    public static Value<Integer> rainbowhue;
    public static Value<Integer> Difference;
    public static Value<Integer> Speed;
    public static Value<Integer> a;
    public static Value<Boolean> ClickguiRainbow;*/

    public static NumberProperty<Integer> Saturation;
    public static NumberProperty<Integer> Brightness;
    public static NumberProperty<Integer> rainbowhue;
    public static NumberProperty<Integer> rainbowSpeed;
    public static NumberProperty<Integer> Difference;
    public static NumberProperty<Integer> Speed;
    public static NumberProperty<Integer> a;
    public static Property<Boolean> ClickguiRainbow;


    public RainbowFeature() {
        /*Saturation = register(new Value<>("Saturation", this, 143, 0, 255));
        Brightness = register(new Value<>("Brightness", this, 215, 0, 255));
        rainbowhue = register(new Value<>("Delay", this, 240, 0, 600));
        Difference = register(new Value<>("Difference", this, 1, 0, 100, v -> false));
        Speed = register(new Value<>("New Speed", this, 1, 0, 100, v -> false));
        a = register(new Value<>("Alpha", this, 1, 0, 255, v -> false));
        ClickguiRainbow = register(new Value<>("ClickGui Rainbow", this, true));*/

        Saturation = new NumberProperty<>(this, "Saturation", "", 0, 143, 255);
        Brightness = new NumberProperty<>(this, "Brightness", "", 0, 215, 255);
        rainbowhue = new NumberProperty<>(this, "Delay", "", 0, 240, 600);
        rainbowSpeed = new NumberProperty<>(this, "Speed2", "", 1, 2, 50);
        Difference = new NumberProperty<>(this, "Difference", "", 0, 1, 100, v -> false);
        Speed = new NumberProperty<>(this, "New Speed", "", 0, 1, 100, v -> false);
        a = new NumberProperty<>(this,"Alpha", "", 0, 1, 255, v -> false);
        ClickguiRainbow = new Property<>(this, "ClickGui Rainbow", "", true);
        /*this.addProperty(this.Saturation);
        this.addProperty(this.Brightness);
        this.addProperty(this.rainbowhue);
        this.addProperty(this.ClickguiRainbow);*/
    }
}

