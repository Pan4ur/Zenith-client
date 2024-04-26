package me.gopro336.zenith.event.client;

import me.gopro336.zenith.event.EventCancellable;
import net.minecraft.client.gui.GuiScreen;

public class LoadGuiEvent extends EventCancellable {

    private GuiScreen gui;

    public LoadGuiEvent(GuiScreen gui) {
        this.gui = gui;
    }

    public GuiScreen getGui() {
        return gui;
    }

    public void setGui(GuiScreen gui) {
        this.gui = gui;
    }
}