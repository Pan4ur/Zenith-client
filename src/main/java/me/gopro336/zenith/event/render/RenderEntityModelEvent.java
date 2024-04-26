package me.gopro336.zenith.event.render;

import me.gopro336.zenith.event.EventCancellable;
import me.gopro336.zenith.event.EventStageable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class RenderEntityModelEvent
extends EventCancellable {
    public ModelBase modelBase;
    public Entity entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float age;
    public float headYaw;
    public float headPitch;
    public float scale;

    public RenderEntityModelEvent(ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        //super(stage);
        this.modelBase = modelBase;
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.age = age;
        this.headYaw = headYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }

    public Entity getEntity() {
        return entity;
    }
    public ModelBase getModel() {
        return modelBase;
    }

    public Float getAge() {
        return age;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadYaw(float yaw) {
        this.headYaw = yaw;
    }

    public float getHeadPitch() {
        return this.headPitch;
    }
    public void setHeadPitch(int p) {
        this.headPitch = p;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(int s) {
        this.scale = s;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }
    public float getLimbSwing() {
        return this.limbSwing;
    }
    public void setHeadPitch(float p) {
        this.headPitch = p;
    }
}

