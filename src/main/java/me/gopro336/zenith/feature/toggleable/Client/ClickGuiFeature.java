package me.gopro336.zenith.feature.toggleable.Client;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@AnnotationHelper(name = "ClickGUI", description = "Toggle modules by clicking on them", category = Category.CLIENT)
public class ClickGuiFeature
extends Feature {
    public static ClickGuiFeature INSTANCE = new ClickGuiFeature();

    //public static Property<Integer> blurInt;

    public static Property<Boolean> bColor;
    public static Property<Boolean> oColor;
    public static Property<Boolean> tColor;
    public static Property<Color> color;
    public static Property<Color> accentColor;
    public static NumberProperty<Integer>  bred;
    public static NumberProperty<Integer> tred;
    public static NumberProperty<Integer> tgreen;
    public static NumberProperty<Integer> tblue;
    public static NumberProperty<Integer> talpha;
    public static Property<Boolean> fullCtrl;
    public static Property<Boolean> textShadow;
    public static Property<Boolean> thin;
    public static Property<Boolean> round;
    public static Property<Boolean> dot;
    public static NumberProperty<Integer> backalpha;
    public static NumberProperty<Integer> disbuttonalpha;
    public static NumberProperty<Integer> hovereffect;
    public static NumberProperty<Integer> hovereffectslide;
    public static NumberProperty<Integer> dropdownSpeed;
    public static Property<String> textBox;

    public ClickGuiFeature() {
        //blurInt = new NumberProperty<>(this, tColor, "BlurIntensity", "", 0, 2, 10);

        bColor = new Property<>(this,"Button Color", "",  true, v -> false);
        oColor = new Property<>(this,"Outline Color", "",  true, v -> false);
        tColor = new Property<>(this,"Text Color", "",  true, v -> fullCtrl.getValue());
        color = new Property<>(this, "Color", "color of the guis", new Color(229, 11, 137, 181));
        accentColor = new Property<>(this, "AccentColor", "accent color of the guis", new Color(0xffdb58));
        tred = new NumberProperty<>(this, tColor, "Text red", "", 0, 255, 255);
        tgreen = new NumberProperty<>(this, tColor, "Text green", "", 0, 255, 255);
        tblue = new NumberProperty<>(this, tColor, "Text blue", "", 0, 255, 255);
        talpha = new NumberProperty<>(this, tColor, "Text alpha", "", 0, 255, 255);
        fullCtrl = new Property<>(this, "FullColorCtrl", "", false);
        textShadow = new Property<>(this, "TextShadow", "", false);
        thin = new Property<>(this,"ThinOutline", "",  false, v -> fullCtrl.getValue());
        round = new Property<>(this,"Rounded Buttons", "",  false, v -> fullCtrl.getValue());
        dot = new Property<>(this,"...", "", true, v -> fullCtrl.getValue());
        backalpha = new NumberProperty<>(this,"BackgroundOpacity", "",  0, 174, 255, v -> fullCtrl.getValue());
        disbuttonalpha = new NumberProperty<>(this,"DisabledOpacity", "",  0, 146, 255, v -> fullCtrl.getValue());
        hovereffect = new NumberProperty<>(this,"HoverEffect", "",  0, 5, 10, v -> fullCtrl.getValue());
        hovereffectslide = new NumberProperty<>(this,"HoverMotionEffect", "",  0, 0, 10, v -> fullCtrl.getValue());
        dropdownSpeed = new NumberProperty<>(this,"AnimationSpeed", "",  1, 5, 30, v -> fullCtrl.getValue());
        textBox = new Property<>(this,"Text", "", "yo wtf");
        //dPercentage = new Value<>("Percent", this, 0, 0, 100));
        this.setInstance();
        //this.getKey().setKeyCode(Keyboard.KEY_P);
        this.setKey(Keyboard.getKeyIndex("P"));
    }

    public static ClickGuiFeature getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGuiFeature();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    //ClickGUI clickGui = new ClickGUI();

    @Override
    public void onEnable() {
/*
        if (nullCheck())
            return;

        super.onEnable();
        ScreenManager.setScreen(clickGui);*/

        this.toggle();

        mc.displayGuiScreen(Zenith.clickGUI);
    }

    @Override
    public void onUpdate() {
        /*if (nullCheck()) {
            this.disable();
        }*/
    }
}

