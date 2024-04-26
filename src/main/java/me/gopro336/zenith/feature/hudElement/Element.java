package me.gopro336.zenith.feature.hudElement;

import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

/**
 * @author Gopro336
 * @since 12/22/2020
 */

public class Element extends Feature
{
    protected final Minecraft mc = Minecraft.getMinecraft();
    public float x;
    public float y;
    public float w;
    public float h;

    public float lastWidth;
    public float lastHeight;
    public float lastX;
    public float lastY;

    private boolean showing;

    public Element() {

    }

    public void onRender() {
        //handleElement();
    }

    public void onUpdate() { }

    public void init() { }

    public void close() { }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return w;
    }

    public void setWidth(float w) {
        this.w = w;
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    /*public void handleElement() {
        setLeftSide(this.getX() + (this.getWidth() / 2.f) < Render2DHelper.INSTANCE.getScaledWidth() / 2.f);
        this.setWidth(Math.max(width, minWidth));
        this.setHeight(Math.max(height, minHeight));
        if (this.isDragging) {
            if (!MouseHelper.INSTANCE.isMouseButtonDown(0)) {
                this.isDragging = false;
            } else {
                x = xDif + MouseHelper.INSTANCE.getMouseX();
                y = yDif + MouseHelper.INSTANCE.getMouseY();
                checkCollisions();
                this.setX(MathHelper.clamp(this.getX(), 0, Render2DHelper.INSTANCE.getScaledWidth() - this.getWidth()));
                this.setY(MathHelper.clamp(this.getY(), 0, Render2DHelper.INSTANCE.getScaledHeight() - this.getHeight()));
            }
        }

        if (!this.isLeftSide()) {
            if (lastWidth > width) {
                float dif = lastWidth - width;
                x += dif;
            } else if (lastWidth < width) {
                float dif = width - lastWidth;
                x -= dif;
            }
        }
        if (!this.isTopSide()) {
            if (lastHeight > height) {
                float dif = lastHeight - height;
                y += dif;
            } else if (lastHeight < height) {
                float dif = height - lastHeight;
                y -= dif;
            }
        }
        if (lastWidth != this.width || lastHeight != this.height) {
            checkCollisionsMoveOthers();
            bringOldCollisions();
        }
        this.lastWidth = this.width;
        this.lastHeight = this.height;
        this.lastX = this.x;
        this.lastY = this.y;
    }*/
/*
    public void checkCollisions() {
        ArrayList<Element> colliding = getCollidingElements();
        if (!colliding.isEmpty()) {
            for (Element hudElement : colliding) {
                if (!hudElement.isVisible())
                    continue;

                if (hudElement.getY() < this.getY() + this.getHeight() && hudElement.getY() > this.getY()) {//bottom touching
                    this.setY(hudElement.getY() - this.getHeight());
                } else if (hudElement.getY() + hudElement.getHeight() > this.getY() && hudElement.getY() + hudElement.getHeight() < this.getY() + this.getHeight()) {//top touching
                    this.setY(hudElement.getY() + hudElement.getHeight());
                } else if (hudElement.getX() + hudElement.getWidth() > this.getX() && hudElement.getX() + hudElement.getWidth() < this.getX() + this.getWidth()) { //left side touching
                    this.setX(hudElement.getX() + hudElement.getWidth());
                } else if (hudElement.getX() < this.getX() + this.getWidth() && hudElement.getX() > this.getX()) {//right side touching
                    this.setX(hudElement.getX() - this.getWidth());
                }
            }
        }
    }

    public void checkCollisionsMoveOthers() {
        ArrayList<Element> colliding = getCollidingElements();
        if (!colliding.isEmpty()) {
            ArrayList<Element> exempt = new ArrayList<>();
            exempt.add(this);
            for (Element hudElement : colliding) {
                if (!hudElement.isVisible())
                    continue;
                if (hudElement.getY() < this.getY() + this.getHeight() && hudElement.getY() > this.getY() && isTopSide() && this.getHeight() > this.lastHeight) {//bottom touching
                    hudElement.setY(this.getY() + this.getHeight());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getY() + hudElement.getHeight() > this.getY() && hudElement.getY() + hudElement.getHeight() < this.getY() + this.getHeight() && !isTopSide() && this.getHeight() > this.lastHeight) {//top touching
                    hudElement.setY(this.getY() - hudElement.getHeight());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getX() + hudElement.getWidth() > this.getX() && hudElement.getX() + hudElement.getWidth() < this.getX() + this.getWidth() && !isLeftSide() && this.getWidth() > this.lastWidth) { //left side touching
                    hudElement.setX(this.getX() - hudElement.getWidth());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getX() < this.getX() + this.getWidth() && hudElement.getX() > this.getX() && isLeftSide() && this.getWidth() > this.lastWidth) {//right side touching
                    hudElement.setX(this.getX() + this.getWidth());
                    hudElement.checkCollisionsMoveOthers(exempt);
                }
            }
        }
    }*/

   /* public void checkCollisionsMoveOthers(ArrayList<Element> exempt) {
        ArrayList<Element> colliding = getCollidingElements();
        exempt.add(this);
        if (!colliding.isEmpty()) {
            for (Element hudElement : colliding) {
                if (!hudElement.isVisible())
                    continue;
                if (exempt.contains(hudElement))
                    continue;
                if (hudElement.getY() < this.getY() + this.getHeight() && hudElement.getY() > this.getY() && isTopSide() && this.getHeight() > this.lastHeight) {//bottom touching
                    hudElement.setY(this.getY() + this.getHeight());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getY() + hudElement.getHeight() > this.getY() && hudElement.getY() + hudElement.getHeight() < this.getY() + this.getHeight() && !isTopSide() && this.getHeight() > this.lastHeight) {//top touching
                    hudElement.setY(this.getY() - hudElement.getHeight());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getX() + hudElement.getWidth() > this.getX() && hudElement.getX() + hudElement.getWidth() < this.getX() + this.getWidth() && !isLeftSide() && this.getWidth() > this.lastWidth) { //left side touching
                    hudElement.setX(this.getX() - hudElement.getWidth());
                    hudElement.checkCollisionsMoveOthers(exempt);
                } else if (hudElement.getX() < this.getX() + this.getWidth() && hudElement.getX() > this.getX() && isLeftSide() && this.getWidth() > this.lastWidth) {//right side touching
                    hudElement.setX(this.getX() + this.getWidth());
                    hudElement.checkCollisionsMoveOthers(exempt);
                }
            }
        }
    }

    public void bringOldCollisions() {
        ArrayList<Element> noLongerColliding = new ArrayList<>();
        ArrayList<Element> colliding = getCollidingElements();
        ArrayList<Element> wascolliding = getWereCollidingElements();
        ArrayList<Element> exempt = new ArrayList<>();
        exempt.add(this);
        for (Element hudElement : wascolliding) {
            if (!colliding.contains(hudElement))
                noLongerColliding.add(hudElement);
        }
        if (!noLongerColliding.isEmpty()) {
            for (Element hudElement : noLongerColliding) {
                if (!hudElement.isVisible())
                    continue;
                if (hudElement.getY() <= this.lastY + this.lastHeight && hudElement.getY() > this.lastY && isTopSide() && this.getHeight() < this.lastHeight) {//bottom touching
                    hudElement.setY(this.getY() + this.getHeight());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getY() + hudElement.getHeight() >= this.lastY && hudElement.getY() + hudElement.getHeight() < this.lastY + this.lastHeight && !isTopSide() && this.getHeight() < this.lastHeight) {//top touching
                    hudElement.setY(this.getY() - hudElement.getHeight());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getX() + hudElement.getWidth() >= this.lastX && hudElement.getX() + hudElement.getWidth() < this.lastX + this.lastWidth && !isLeftSide() && this.getWidth() < this.lastWidth) { //left side touching
                    hudElement.setX(this.getX() - hudElement.getWidth());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getX() <= this.lastX + this.lastWidth && hudElement.getX() > this.lastX && isLeftSide() && this.getWidth() < this.lastWidth) {//right side touching
                    hudElement.setX(this.getX() + this.getWidth());
                    hudElement.bringOldCollisions(exempt);
                }
            }
        }
    }

    public void bringOldCollisions(ArrayList<Element> exempt) {
        ArrayList<Element> noLongerColliding = new ArrayList<>();
        ArrayList<Element> colliding = getCollidingElements();
        ArrayList<Element> wascolliding = getWereCollidingElements();
        exempt.add(this);
        for (Element hudElement : wascolliding) {
            if (!colliding.contains(hudElement))
                noLongerColliding.add(hudElement);
        }
        if (!noLongerColliding.isEmpty()) {
            for (Element hudElement : noLongerColliding) {
                if (!hudElement.isVisible())
                    continue;
                if (exempt.contains(hudElement))
                    continue;
                if (hudElement.getY() <= this.lastY + this.lastHeight && hudElement.getY() > this.lastY && isTopSide() && this.getHeight() < this.lastHeight) {//bottom touching
                    hudElement.setY(this.getY() + this.getHeight());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getY() + hudElement.getHeight() >= this.lastY && hudElement.getY() + hudElement.getHeight() < this.lastY + this.lastHeight && !isTopSide() && this.getHeight() < this.lastHeight) {//top touching
                    hudElement.setY(this.getY() - hudElement.getHeight());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getX() + hudElement.getWidth() >= this.lastX && hudElement.getX() + hudElement.getWidth() < this.lastX + this.lastWidth && !isLeftSide() && this.getWidth() < this.lastWidth) { //left side touching
                    hudElement.setX(this.getX() - hudElement.getWidth());
                    hudElement.bringOldCollisions(exempt);
                } else if (hudElement.getX() <= this.lastX + this.lastWidth && hudElement.getX() > this.lastX && isLeftSide() && this.getWidth() < this.lastWidth) {//right side touching
                    hudElement.setX(this.getX() + this.getWidth());
                    hudElement.bringOldCollisions(exempt);
                }
            }
        }
    }

    public ArrayList<Element> getCollidingElements() {
        ArrayList<Element> list = new ArrayList<>();
        for (Element hudElement : FeatureManager.getHudElements()) {
            if (hudElement == this || !hudElement.isVisible() || !hudElement.isEnabled())
                continue;
            if (hudElement.getX() + hudElement.getWidth() >= this.getX() && hudElement.getX() <= this.getX() + this.getWidth()) {
                if (hudElement.getY() + hudElement.getHeight() >= this.getY() && hudElement.getY() <= this.getY() + this.getHeight())
                    list.add(hudElement);
            }
        }
        return list;
    }

    public ArrayList<Element> getWereCollidingElements() {
        ArrayList<Element> list = new ArrayList<>();
        for (Element hudElement : FeatureManager.getElements()) {
            if (hudElement == this || !hudElement.isVisible() || !hudElement.isEnabled())
                continue;
            if (hudElement.getX() + hudElement.getWidth() >= this.lastX && hudElement.getX() <= lastX + this.lastWidth) {
                if (hudElement.getY() + hudElement.getHeight() >= this.lastY && hudElement.getY() <= lastY + this.lastHeight)
                    list.add(hudElement);
            }
        }
        return list;
    }*/
}
