package me.gopro336.zenith.userInterface.clickgui;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.userInterface.clickgui.button.ModuleComponent;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.userInterface.clickgui.button.buttons.*;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;

import java.util.ArrayList;

/**
 * @author Gopro336
 * @author Danmaster2
 */
public class Dropdown implements IGlobals {

    private final int W;
    private final int H;
    public int X;
    public int Y;
    private final Feature feature;
    private final ModuleComponent moduleComponent;
    private static int modY = 0;

    private final ArrayList<SettingComponent> buttons = new ArrayList<>();

    public Dropdown(Feature feature, ModuleComponent mButton, int x, int y, int w, int h) {
        X = x;
        Y = y;
        W = w;
        H = h;

        this.feature = feature;
        this.moduleComponent = mButton;
        int boost = 0;

        initGui(boost);
    }

    public SettingComponent getPropertyButton(Property<?> property, int boost, boolean sub) {
        SettingComponent SettingComponent = null;

        if (property.getValue() instanceof Boolean)
            SettingComponent = new BoolSettingComponent(feature, (Property<Boolean>) property, X, Y + (boost * H), W, H, sub);
        if (property.getValue() instanceof Enum)
            SettingComponent = new EnumSettingComponent(feature, (Property<Enum<?>>)property, X, Y + (boost * H), W, H,sub);
        /*if (property.getValue() instanceof Color)
            SettingComponent = new ColorSettingComponent(feature, (Property<Color>)property, X, Y + (boost * H), W, H,sub);*/
        if (property.getValue() instanceof Number)
            SettingComponent = new SliderSettingComponent(feature, (NumberProperty<Number>) property, X, Y + (boost * H), W, H,sub);

        if (SettingComponent != null)
            buttons.add(SettingComponent);

        return SettingComponent;
    }

    public void initGui(int boost) {
        if (Zenith.SettingManager.getPropertiesByMod(feature) == null) return;

//        for (Property<?> property : FeatureManager.getOldPropertiesFromFeature(feature)) {
        for (Property<?> property : Zenith.SettingManager.getPropertiesByMod(feature)) {
            /*if there is is not a parent Property for this Property, move on to next iteration
            (this is done so we don't have sub-Properties being treated as normal Properties. sub-Properties are called later on)*/
            if (property.isSubProperty()) continue;
            SettingComponent SettingComponent = getPropertyButton(property, boost, false);
            if (SettingComponent != null)
                for (Property<?> sub : property.getSubProperties()) {
                    SettingComponent.countSub();
                    getPropertyButton(sub, boost, true);
                }
        }

        buttons.add(new BindSettingComponent(feature, X, Y + H + (boost * H), W, H, false));
    }

    public void onRender(int mX, int mY, float partialTicks) {
        modY = 0;
        int boost = 0;
        for (SettingComponent button : buttons) {
            if (button.isHidden()) continue;
            //if there is no parent Property render as normal
            if (button instanceof BindSettingComponent || !button.getProperty().isSubProperty()) {
                boost += button.getYOffset();
                button.setPosX(X);
                button.setPosY(Y + boost);
                button.onRender(mX, mY, partialTicks);

            }   else if (button.getProperty().isSubProperty()) {
                //check if the parent Property is open, if it is not open continue to next iteration
                if (!button.getProperty().getParentProperty().isVisible()) continue;
                if (!button.getProperty().getParentProperty().isExtended()) continue;
                boost += button.getYOffset();
                //add visibility thingy here
                button.setPosX(X);
                button.setPosY(Y + boost);
                button.onRender(mX, mY, partialTicks);

            }
            //check if the parent Property is open, if it is not open continue to next iteration

            modY = boost;
        }
    }

    public void onUpdate() {
        buttons.forEach(SettingComponent::onUpdate);
    }

    public void setX(int x) {
        X = x;
    }

    public double getH() {
        return H;
    }

    public void setY(int y) {
        Y = y;
    }

    public ArrayList<SettingComponent> getButtons() {
        return buttons;
    }

    /**
     * dropdown percentage is used for dropdown animations, they are currently not functioning
     */
    public int getBoost() {
        //return (int) (moduleButton.getDropdownProgressPercentage() * (modY * H)) / 100;
        //return (int)(modY*H);
        return modY;
    }

}