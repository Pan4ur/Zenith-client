package me.gopro336.zenith.property;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.feature.Feature;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Gopro336
 */
public class Property<T> {
    private final String name;
    private final String description;
    private boolean extended;
    private Feature feature;

    public Property<?> parentProperty = null;
    public boolean isSubProperty = false;

    public Mode getMode() {
        return mode;
    }

    private Mode mode;
    private T value;
    private float alpha = 0.2f;

    private final ArrayList<Property<?>> subProperties = new ArrayList<>();
    public Consumer<OnChangedValue<T>> changeTask = null;
    private Predicate<T> visibleCheck = null;

    public Property(Feature feature, String name, String description, T value) {
        this.name = name;
        this.extended = false;

        this.description = description;
        this.value = value;
        this.feature = feature;
        Zenith.SettingManager.add(this);

        if (value instanceof Boolean) {
            this.mode = Mode.BOOLEAN;
        } else if (value instanceof Color) {
            this.mode = Mode.COLOR;
        } else if (value instanceof String) {
            this.mode = Mode.STRING;
        }  else if (value instanceof Enum) {
            this.mode = Mode.ENUM;
        } else if (value instanceof Number) {
            this.mode = Mode.NUMBER;
        }
    }

    // for sub Properties
    public Property(Feature feature, Property<?> parent, String name, String description, T value) {
        this.name = name;
        this.extended = false;
        this.description = description;
        this.value = value;
        this.isSubProperty = true;
        this.parentProperty = parent;
        this.feature = feature;
        Zenith.SettingManager.add(this);

        if (parent.getValue() instanceof Boolean) {
            Property<Boolean> booleanProperty = (Property<Boolean>) parent;
            booleanProperty.addSubProperty(this);
            this.mode = Mode.BOOLEAN;
        }

        if (parent.getValue() instanceof String) {
            Property<String> stringProperty = (Property<String>) parent;
            stringProperty.addSubProperty(this);
            this.mode = Mode.STRING;
        }

        if (parent.getValue() instanceof Enum) {
            Property<Enum<?>> enumProperty = (Property<Enum<?>>) parent;
            enumProperty.addSubProperty(this);
            this.mode = Mode.ENUM;
        }

        if (parent.getValue() instanceof Color) {
            Property<Color> colorProperty = (Property<Color>) parent;
            colorProperty.addSubProperty(this);
            this.mode = Mode.COLOR;
        }

        if (parent.getValue() instanceof Integer) {
            NumberProperty<Integer> integerNumberProperty = (NumberProperty<Integer>) parent;
            integerNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }

        if (parent.getValue() instanceof Double) {
            NumberProperty<Double> doubleNumberProperty = (NumberProperty<Double>) parent;
            doubleNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }

        if (parent.getValue() instanceof Float) {
            NumberProperty<Float> floatNumberProperty = (NumberProperty<Float>) parent;
            floatNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }
    }

    // For visibility predicate
    public Property(Feature feature, String name, String description, T value, Predicate<T> visibility) {
        this.name = name;
        this.extended = false;
        this.description = description;
        this.value = value;
        this.visibleCheck = visibility;
        this.feature = feature;
        Zenith.SettingManager.add(this);

        if (value instanceof Boolean) {
            this.mode = Mode.BOOLEAN;
        } else if (value instanceof Color) {
            this.mode = Mode.COLOR;
        } else if (value instanceof String) {
            this.mode = Mode.STRING;
        } else if (value instanceof Enum) {
            this.mode = Mode.ENUM;
        } else if (value instanceof Number) {
            this.mode = Mode.NUMBER;
        }

    }

    // for sub Properties
    public Property(Feature feature, Property<?> parent, String name, String description, T value, Predicate<T> visibility) {
        this.name = name;
        this.extended = false;
        this.description = description;
        this.value = value;
        this.visibleCheck = visibility;
        this.isSubProperty = true;
        this.parentProperty = parent;
        this.feature = feature;
        Zenith.SettingManager.add(this);

        if (parent.getValue() instanceof Boolean) {
            Property<Boolean> booleanProperty = (Property<Boolean>) parent;
            booleanProperty.addSubProperty(this);
            this.mode = Mode.BOOLEAN;
        }

        if (parent.getValue() instanceof String) {
            Property<String> stringProperty = (Property<String>) parent;
            stringProperty.addSubProperty(this);
            this.mode = Mode.STRING;
        }

        if (parent.getValue() instanceof Enum) {
            Property<Enum<?>> enumProperty = (Property<Enum<?>>) parent;
            enumProperty.addSubProperty(this);
            this.mode = Mode.ENUM;
        }

        if (parent.getValue() instanceof Color) {
            Property<Color> colorProperty = (Property<Color>) parent;
            colorProperty.addSubProperty(this);
            this.mode = Mode.COLOR;
        }

        if (parent.getValue() instanceof Integer) {
            NumberProperty<Integer> integerNumberProperty = (NumberProperty<Integer>) parent;
            integerNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }

        if (parent.getValue() instanceof Double) {
            NumberProperty<Double> doubleNumberProperty = (NumberProperty<Double>) parent;
            doubleNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }

        if (parent.getValue() instanceof Float) {
            NumberProperty<Float> floatNumberProperty = (NumberProperty<Float>) parent;
            floatNumberProperty.addSubProperty(this);
            this.mode = Mode.NUMBER;
        }
    }

    public void setValueEnumString(final String value) {
        Enum[] array;
        for (int length = (array = (Enum[])this.getValue().getClass().getEnumConstants()).length, i = 0; i < length; ++i) {
            if (array[i].name().equalsIgnoreCase(value)) {
                this.value = (T)array[i];
            }
        }
    }

    public String getFixedValue() {
        return Character.toString(getName().charAt(0)) + getName().toLowerCase().replaceFirst(Character.toString(getName().charAt(0)).toLowerCase(), "");
    }

    public boolean isBoolean(){
        return mode == Mode.BOOLEAN;
    }

    public boolean isString(){
        return mode == Mode.STRING;
    }

    public boolean isEnum(){
        return mode == Mode.ENUM;
    }

    public boolean isColor(){
        return mode == Mode.COLOR;
    }

    public boolean isNumber(){
        return mode == Mode.NUMBER;
    }

    enum Mode {
        BOOLEAN,
        STRING,
        ENUM,
        COLOR,
        NUMBER
    }

    public void setValue(T value) {
        T old = this.value;
        this.value = value;
        if (changeTask != null) {
            changeTask.accept(new OnChangedValue<>(old, value));
        }
    }

    public Property<T> onChanged(Consumer<OnChangedValue<T>> run) {
        this.changeTask = run;
        return this;
    }

    public boolean isVisible() {
        if (visibleCheck == null) return true;
        return visibleCheck.test(value);
    }

    public Property<?> getParentProperty() {
        return this.parentProperty;
    }

    public void addSubProperty(Property<?> subProperty) {
        this.subProperties.add(subProperty);
    }

    public T getValue() {
        return value;
    }

    public void toggleOpenState() {
        this.extended = !this.extended;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public boolean isSubProperty(){
        return isSubProperty;
    }

    public ArrayList<Property<?>> getSubProperties() {
        return this.subProperties;
    }

    public boolean hasSubProperties() {
        return this.subProperties.size() > 0;
    }

    public void addSubSetting(Property<?> subProperty) {
        this.subProperties.add(subProperty);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
