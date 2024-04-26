package me.gopro336.zenith.feature.toggleable;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;

import javax.annotation.processing.SupportedAnnotationTypes;

/**
 * fake autocrystal kek
 */
@AnnotationHelper(name = "AutoCrystal", category = Category.COMBAT)
public class Dummy extends Feature {
    @Override
    public String getFeatureMetadata() {
        return "Sequential";
    }
}
