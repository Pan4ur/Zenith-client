package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AnnotationHelper(name = "Fullbright", description = "Makes it fully bright", category = Category.RENDER)
public class Fullbright extends Feature {

    private final List<Float> previousLevels = new ArrayList<>();

    @Override
    public void onEnable() {
        if(mc.player != null && mc.world != null) {
            final float[] table = mc.world.provider.getLightBrightnessTable();
            if (mc.world.provider != null) {
                for (float f : table) {
                    previousLevels.add(f);
                }

                Arrays.fill(table, 1f);
            }
        }else {
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if(mc.player != null && mc.world != null) {
            final float[] table = mc.world.provider.getLightBrightnessTable();
            for (int i = 0; i < table.length; i++) {
                table[i] = previousLevels.get(i);
            }

            previousLevels.clear();
        }
    }
}