package me.gopro336.zenith.property.propertyrw;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author Gopro336
 * @since 11/1/21
 */
public class PropertyWR<T> {
    public String name;
    public String description = "";
    public T value;
    public T minimum;
    public T maximum;
    private boolean opened;
    private Feature feature;
    public BooleanSupplier isVis = PropertyWR::isTrue;
    public PropertyWR<PropertyWR<?>> parentProperty = null;
    public Consumer<OnChangedValue<T>> changeTask = null;

    public PropertyWR(String string, T value) {
        this.name = string;
        this.value = value;
    }

    public PropertyWR(String string, T min, T value, T max) {
        this.name = string;
        this.value = value;
        this.maximum = max;
        this.minimum = min;
    }

    public PropertyWR<T> withParent(PropertyWR<PropertyWR<?>> property) {
        parentProperty = property;
        return this;
    }

    public PropertyWR<T> withDescription(String string) {
        this.description = string;
        return this;
    }

    public PropertyWR<T> withVisibility(BooleanSupplier booleanSupplier) {
        isVis = booleanSupplier;
        return this;
    }

    public void setValue(T value) {
        T old = this.value;

        if (this.minimum != null && this.maximum != null) {
            T number = value;
            Number number2 = (Number)this.minimum;
            Number number3 = (Number)this.maximum;
            this.value = number;
        } else {
            this.value = value;
        }

        if (changeTask != null) {
            changeTask.accept(new OnChangedValue<>(old, value));
        }
    }

    public PropertyWR<T> onChanged(Consumer<OnChangedValue<T>> run) {
        this.changeTask = run;
        return this;
    }

    public boolean isInvisible() {
        if (parentProperty != null && parentProperty.getValue().isOpened()) {
            return false;
        }
        return this.isVis.getAsBoolean();
    }

    public Feature getParentFeature() {
        for (Feature feature : Zenith.featureManager.getModules()) {
            if (FeatureManager.isPropertyInFeature(feature.getName(), getName()) == null) continue;
            return feature;
        }
        return null;
    }

    public static boolean isTrue() {
        return true;
    }

    /*public boolean isVisible() {
        if (visibleCheck == null) return true;
        return visibleCheck.test(value);
    }*/

    public T getValue() {
        return value;
    }

    public T getMinimum(){
        return minimum;
    }

    public T getMaximum(){
        return maximum;
    }

    public void toggleOpenState() {
        this.opened = !this.opened;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSubProperty() {
        return parentProperty != null;
    }

    public PropertyWR<PropertyWR<?>> getParentProperty() {
        return parentProperty;
    }
}
