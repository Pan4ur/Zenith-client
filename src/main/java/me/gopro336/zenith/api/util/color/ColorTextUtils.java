package me.gopro336.zenith.api.util.color;

import static me.gopro336.zenith.api.util.color.ColorTextUtils.colors.*;

public class ColorTextUtils {

    public enum colors{
        White,
        Black,
        Blue,
        Green,
        Cyan,
        Red,
        Purple,
        Gold,
        LightGray,
        Gray,
        Lavender,
        LightGreen,
        LightBlue,
        LightRed,
        Pink,
        Yellow,
    }

    public static String getColor(colors value) {
        String prefix;
        if (value.equals(White)) {
            prefix = "&f";
        }
        else if (value.equals(Red)) {
            prefix = "&4";
        }
        else if (value.equals(Blue)) {
            prefix = "&1";
        }
        else if (value.equals(Cyan)) {
            prefix = "&3";
        }
        else if (value.equals(Pink)) {
            prefix = "&d";
        }
        else if (value.equals(Black)) {
            prefix = "&0";
        }
        else if (value.equals(Green)) {
            prefix = "&2";
        }
        else if (value.equals(Purple)) {
            prefix = "&5";
        }
        else if (value.equals(Yellow)) {
            prefix = "&e";
        }
        else if (value.equals(LightRed)) {
            prefix = "&c";
        }
        else if (value.equals(LightBlue)) {
            prefix = "&b";
        }
        else if (value.equals(LightGreen)) {
            prefix = "&a";
        }
        else if (value.equals(Gold)) {
            prefix = "&6";
        }
        else if (value.equals(Gray)) {
            prefix = "&8";
        }
        else if (value.equals(Lavender)) {
            prefix = "&9";
        }
        else if (value.equals(LightGray)) {
            prefix = "&7";
        }
        else {
            prefix = "&r";
        }
        return prefix;
    }
}
