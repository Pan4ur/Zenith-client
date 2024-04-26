package me.gopro336.zenith.property;

/**
 * @author Gopro336
 */

public final class EnumUtil {
    public static String getNextEnumValue(Property<Enum<?>> Property, boolean reverse) {
        Enum<?> currentValue = Property.getValue();

        int i = 0;

        for (; i < Property.getValue().getClass().getEnumConstants().length; i++) {
            Enum<?> e = Property.getValue().getClass().getEnumConstants()[i];

            if (e.name().equalsIgnoreCase(currentValue.name())) break;
        }

        return Property.getValue().getClass().getEnumConstants()[(reverse ? (i != 0 ? i - 1 : Property.getValue().getClass().getEnumConstants().length - 1) : i + 1) % Property.getValue().getClass().getEnumConstants().length].toString();
    }

    public static void setEnumValue(Property<Enum<?>> Property, String value) {
        for (Enum<?> e : Property.getValue().getClass().getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value)) {
                Property.setValue(e);

                break;
            }
        }
    }
}
