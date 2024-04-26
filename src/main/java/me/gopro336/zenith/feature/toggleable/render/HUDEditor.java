package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import net.minecraft.client.gui.GuiScreen;

@AnnotationHelper(name = "HUDEditor", category = Category.CLIENT)
public class HUDEditor extends Feature {

    public HUDEditor() {
    }

//    @Override
//    public void onEnable() {
//        this.toggle();
//        mc.displayGuiScreen((GuiScreen) Zenith.hudEditor);
//    }
}

