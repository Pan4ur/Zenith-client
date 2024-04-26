package me.gopro336.zenith.property.propertyrw;

import net.minecraft.entity.Entity;

/**
 * @author Gopro336
 * @since 11/1/21
 */
public class EntityPreview {
    public Entity entity;

    public EntityPreview(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity(){
        return entity;
    }
}
