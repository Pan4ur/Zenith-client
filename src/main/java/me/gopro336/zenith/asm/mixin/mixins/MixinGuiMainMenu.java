package me.gopro336.zenith.asm.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Gopro336
 */
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @Inject(method={"drawScreen"}, at={@At(value="TAIL")}, cancellable=true)
    public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft.getMinecraft().fontRenderer.drawString("Nigga", 100, 100, 0xffffff);
    }

    @Inject(method = "initGui", at = @At(value = "RETURN"), cancellable = true)
    public void initGui(CallbackInfo info) {
        this.buttonList.add(new GuiButton(932, 5, 55, fontRenderer.getStringWidth("2b2tpvp.net") + 10, 20, "2b2tpvp.net"));
        this.buttonList.add(new GuiButton(284, 5, 75, fontRenderer.getStringWidth("2b2t.org") + 10, 20, "2b2t.org"));
    }

    @Inject(method = "actionPerformed", at = @At(value = "HEAD"), cancellable = true)
    public void actionPerformed(GuiButton button, CallbackInfo info) {
        if(button.id == 932) {
            this.mc.displayGuiScreen(new GuiConnecting(this, mc, "2b2tpvp.net", 25565)); //th﻿is n﻿umber is port﻿
        }
        if(button.id == 284) {
            this.mc.displayGuiScreen(new GuiConnecting(this, mc, "2b2t.org", 25565)); //th﻿is n﻿umber is port﻿
        }
    }

}
