package me.gopro336.zenith.feature;

import me.gopro336.zenith.property.Property;

import java.util.ArrayList;

/**
 * @author Danmaster2
 * When I swap in the rewritten settings were no longer going to use this.  -Gopro
 */
@Deprecated
public class SettingManager {

    public ArrayList<Property<?>> getProperties() {
        return Properties;
    }

    public ArrayList<Property<?>> getPropertiesByMod(Feature mod){
        ArrayList<Property<?>> out = new ArrayList<>();
        for(Property<?> s : getProperties()){
            if(s.getFeature().equals(mod)){
                out.add(s);
            }
        }
        return out;
    }

    private final ArrayList<Property<?>> Properties = new ArrayList<>();

    public <T> void add(Property<T> tProperty) {
        Properties.add(tProperty);
    }
}
