package me.gopro336.zenith.property;

import me.gopro336.zenith.feature.Feature;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Gopro336
 */
public class NumberProperty<T extends Number> extends Property<T>{
    private final T min;
    private final T max;

    public NumberProperty(Feature feature, String name, String description, T min, T value, T max) {
        super(feature,name, description, value);

        this.min = min;
        this.max = max;
    }

    public NumberProperty(Feature feature, Property<?> parent, String name, String description, T min, T value, T max) {
        super(feature,parent, name, description, value);

        this.isSubProperty = true;
        this.min = min;
        this.max = max;
    }

    public NumberProperty(Feature feature, String name, String description, T min, T value, T max, Predicate<T> visibility) {
        super(feature,name, description, value);

        this.min = min;
        this.max = max;
    }

    public NumberProperty(Feature feature, Property<?> parent, String name, String description, T min, T value, T max, Predicate<T> visibility) {
        super(feature, parent, name, description, value);

        this.isSubProperty = true;
        this.min = min;
        this.max = max;
    }

    public NumberProperty<T> onChanged(Consumer<OnChangedValue<T>> run) {
        this.changeTask = run;
        return this;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}
