package me.gopro336.zenith.feature.toggleable.misc;

import me.gopro336.zenith.event.client.LoadGuiEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.userInterface.screen.menu.ZenithGuiDisconnected;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.event.world.WorldEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "AutoReconnect", category = Category.MISC)
public class AutoReconnect extends Feature {

    private ServerData lastServer;

    public NumberProperty<Integer> delay = new NumberProperty<>(this, "Delay", "", 1, 5, 30);

    @Listener
    public void onLoadGui(LoadGuiEvent event) {
        if(event.getGui() instanceof GuiDisconnected) {
            updateLastServer();
            event.setGui(new ZenithGuiDisconnected((GuiDisconnected) event.getGui(), lastServer, delay.getValue()));
        }
    }

    @Listener
    public void onWorldUnload(WorldEvent.Unload event) {
        updateLastServer();
    }

    private void updateLastServer() {
        ServerData data = mc.getCurrentServerData();
        if(data != null) {
            lastServer = data;
        }
    }

}