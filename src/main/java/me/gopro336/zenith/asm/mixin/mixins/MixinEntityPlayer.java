package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.event.player.PlayerTravelEvent;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.toggleable.render.RotationViewer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer
        extends MixinEntityLivingBase {

    @Inject(method={"travel"}, at={@At(value="HEAD")}, cancellable=true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        if (this.equals(Minecraft.getMinecraft().player)) {
            PlayerTravelEvent event = new PlayerTravelEvent(strafe, vertical, forward);
            Zenith.INSTANCE.getEventManager().dispatchEvent(event);
            if (event.isCanceled()) {
                move(MoverType.SELF, motionX, motionY, motionZ);
                info.cancel();
            }
        }
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    @Shadow protected abstract void setModelVisibilities(AbstractClientPlayer clientPlayer);

    private float
            renderPitch,
            renderYaw,
            renderHeadYaw,
            prevRenderHeadYaw,
            lastRenderHeadYaw = 0,
            prevRenderPitch,
            lastRenderPitch = 0;

    @Inject(method = "doRender", at = @At("HEAD"))
    private void rotateBegin(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (FeatureManager.getFeatureByClass(RotationViewer.class).isEnabled()
                && entity == Minecraft.getMinecraft().player) {
            prevRenderHeadYaw = entity.prevRotationYawHead;
            prevRenderPitch = entity.prevRotationPitch;
            renderPitch = entity.rotationPitch;
            renderYaw = entity.rotationYaw;
            renderHeadYaw = entity.rotationYawHead;
            entity.rotationPitch = RotationViewer.getPitch();
            entity.prevRotationPitch = lastRenderPitch;
            entity.rotationYaw = RotationViewer.getYaw();
            entity.rotationYawHead = RotationViewer.getYaw();
            entity.prevRotationYawHead = lastRenderHeadYaw;
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private void rotateEnd(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (FeatureManager.getFeatureByClass(RotationViewer.class).isEnabled()
                && entity == Minecraft.getMinecraft().player) {
            lastRenderHeadYaw = entity.rotationYawHead;
            lastRenderPitch = entity.rotationPitch;
            entity.rotationPitch = renderPitch;
            entity.rotationYaw = renderYaw;
            entity.rotationYawHead = renderHeadYaw;
            entity.prevRotationYawHead = prevRenderHeadYaw;
            entity.prevRotationPitch = prevRenderPitch;
        }
    }
}
