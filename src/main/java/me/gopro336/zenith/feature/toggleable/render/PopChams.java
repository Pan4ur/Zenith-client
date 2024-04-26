package me.gopro336.zenith.feature.toggleable.render;

import com.mojang.authlib.GameProfile;
import me.gopro336.zenith.api.util.math.AnimationUtil;
import me.gopro336.zenith.event.entity.TotemPopEvent;
import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.event.render.RenderEntityModelEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * todo: complete
 */
@AnnotationHelper(name = "PopChams", description = "", category = Category.RENDER)
public class PopChams extends Feature {

    private NumberProperty<Float> fadeSpeed = new NumberProperty<>(this, "Speed", "", 0.2f, 1.0f, 3.0f);

    public static List<Ghost> popGhosts = new ArrayList<>();

    @Listener
    public void onRender3D(Render3DEvent event){
        for (Ghost ghost : popGhosts){
            if (ghost.alpha > 0){
                ghost.alpha = AnimationUtil.moveTowards(ghost.alpha, 0, (0.01f + fadeSpeed.getValue() / 30), 0.1f);
            }
            if (ghost.alpha == 0) popGhosts.remove(ghost);

            RenderEntityModelEvent modelEvent = new RenderEntityModelEvent(ghost.modelPlayer, ghost, ghost.limbSwing, ghost.limbSwingAmount, ghost.ticksExisted, ghost.rotationYawHead, ghost.rotationPitch, 1);

            ChamsRw.onRenderLivingEntity(modelEvent);
        }
    }

    @Listener
    public void onTotemPop(TotemPopEvent event){
        popGhosts.add((Ghost) event.getEntity());
    }

    public class Ghost extends EntityPlayer{

        float alpha;
        private final ModelPlayer modelPlayer;

        public Ghost(World worldIn, GameProfile gameProfileIn) {
            super(worldIn, gameProfileIn);
            this.modelPlayer = new ModelPlayer(0, false);
            this.alpha = 180;
        }

        @Override
        public boolean isSpectator() {
            return false;
        }

        @Override
        public boolean isCreative() {
            return false;
        }
    }

}
