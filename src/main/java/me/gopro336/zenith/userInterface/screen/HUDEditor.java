package me.gopro336.zenith.userInterface.screen;

import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.userInterface.clickgui.Frame;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.hudElement.Element;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.util.TurokDisplay;
import net.minecraft.client.gui.GuiScreen;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import me.gopro336.zenith.Zenith;


import java.awt.*;

/**
 * @author Gopro336
 * @since 12/24/2020
 */

public class HUDEditor extends GuiScreen
{/*
    public final ArrayList<Frame> windows = new ArrayList<>();
    public TurokDisplay display;
    public TurokMouse mouse;

    public HUDEditor() {
        int xOffset = 3;
        Frame window = new Frame(Category.HUD, xOffset, 3, 105, 15);
        windows.add(window);

    }

    private boolean dragging;
    private int dragX;
    private int dragY;
    private Element dragElement;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        doScroll();
        display = new TurokDisplay(mc);

        windows.forEach(window -> window.updatePosition(mouseX, mouseY));
        windows.forEach(window -> window.onRender(mouseX, mouseY));

        for (Element element : FeatureManager.getHudElements()) {
            if (dragging && dragElement.equals(element)) {
                element.setX(mouseX - dragX);
                element.setY(mouseY - dragY);
            }
            if (!Zenith.featureManager.getModule(element.getName()).isEnabled()) continue;
            RenderUtils2D.drawRect(element.getX() - 2, element.getY() - 2, element.getX() + element.getWidth() + 2, element.getY() + element.getHeight() + 2, isHover(element.getX(), element.getY(), element.getWidth(), element.getHeight(), mouseX, mouseY) ? new Color(0x72000000, true).getRGB() : new Color(0x4F000000, true).getRGB());
            element.onRender();
        }
        GuiUtil.mouseListen(mouseX, mouseY);
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (Frame window : windows) {
            window.mouseDown(mouseX, mouseY, mouseButton);
            if (window.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                window.setDragging(true);
                window.dragX = mouseX - window.getPosX();
                window.dragY = mouseY - window.getPosY();
            }
        }
        for (Element element : FeatureManager.getHudElements()) {
            if (Zenith.featureManager.getModule(element.getName()).isEnabled() && isHover(element.getX() - 2, element.getY() - 2, element.getWidth() + 2, element.getHeight() + 2, mouseX, mouseY)) {
                dragElement = element;
                dragging = true;

                dragX = mouseX - (int)element.getX();
                dragY = mouseY - (int)element.getY();
            }
        }
        if (mouseButton == 0) {
            GuiUtil.lclickListen();
        }
        if (mouseButton == 1) {
            GuiUtil.rclickListen();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (state == 0) {
            GuiUtil.releaseListen();
        }
        for (Frame window : windows)
        {
            window.setDragging(false);
            //window.mouseUp(mouseX, mouseY);
        }

        dragging = false;
        dragElement = null;
    }

   /* @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        GuiUtil.keyListen(keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            mc.displayGuiScreen(null);

            if (mc.currentScreen == null)
            {
                mc.setIngameFocus();
            }
        }
    }*/

  /*  @Override
    public void onGuiClosed() {
        dragElement = null;
        dragging = false;
        windows.forEach(frame -> frame.setDragging(false));

        Zenith.featureManager.getModule("HUDEditor").disable();
    }

    private void doScroll()
    {
        int w = Mouse.getDWheel();
        if (w < 0) {
            windows.forEach(window -> window.setPosY(window.getPosY() - 8));
        }
        else if (w > 0) {
            windows.forEach(window -> window.setPosY(window.getPosY() + 8));
        }
    }

    public void onUpdate() {
        windows.forEach(Frame::onUpdate);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    private boolean isHover(float X, float Y, float W, float H, float mX, float mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }*/
}