package me.gopro336.zenith.property;

import me.gopro336.zenith.feature.Feature;

import java.util.function.Consumer;

/**
 * @author Gopro336
 */
public class EntityPreview<T extends EntityPreview.preview> extends Property<T>{

    public EntityPreview(Feature feature, String name, String description, T value) {
        super(feature, name, description, value);
    }

    public EntityPreview(Feature feature, Property<?> parent, String name, String description, T value) {
        super(feature, parent, name, description, value);
        this.isSubProperty = true;
    }

    public EntityPreview<T> onChanged(Consumer<OnChangedValue<T>> run) {
        this.changeTask = run;
        return this;
    }

    public enum preview {
        Crystal,
        Player
    }
}
