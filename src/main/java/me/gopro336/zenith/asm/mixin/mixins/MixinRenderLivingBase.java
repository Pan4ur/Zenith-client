package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.feature.toggleable.render.NewChams;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLivingBase.class)
//public abstract class MixinRenderLivingBase extends Render<T extends Entity> {
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Shadow
    protected abstract void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract boolean isVisible(T var1);

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/RenderLivingBase;renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V"))
    public void doRender(RenderLivingBase renderLivingBase, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        //Class129 pre = new Class129(renderLivingBase.getMainModel(), (Entity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        /*if (!Class511.Field1342) {
            //EventDispatcher.Companion.dispatch(pre);
        }*/
        //if (!pre.Method1234()) {//is not cancelled
        if (true) {
            if (NewChams.INSTANCE.isEnabled()/* || Class167.Method1610(Class511.class).Method1651()*/) {
                boolean flag1;
                boolean flag = this.isVisible(entity);
                boolean bl = flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);
                if (flag || flag1) {
                //if (flag) {
                    if (!this.bindEntityTexture(entity)) {
                        /*if (!Class511.Field1342) {
                            //Class122 post = new Class122(renderLivingBase.getMainModel(), (Entity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                            //Zenith.INSTANCE.getEventManager().dispatchEvent(post);
                        }*/
                        return;
                    }
                    if (flag1) {
                        GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
                    }
                    if ((NewChams.INSTANCE.isEnabled() || NewChams.Field2147) /*&& !Class511.Field1342 */&& NewChams.renderEntityChams(this.mainModel, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor)) {
                        /*if (!Class511.Field1342) {
                            //Class122 post = new Class122(renderLivingBase.getMainModel(), (Entity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                            //Zenith.INSTANCE.getEventManager().dispatchEvent(post);
                        }*/
                        return;
                    }
                    this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                    if (flag1) {
                        GlStateManager.disableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
                    }
                }
            } else {
                this.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
        }
        /*if (!Class511.Field1342) {
            //Class122 post = new Class122(renderLivingBase.getMainModel(), (Entity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            //Zenith.INSTANCE.getEventManager().dispatchEvent(post);
        }*/
    }
/*
    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    public void Method37(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (entity instanceof EntityPlayer) {
            Class511.Method1326((EntityPlayer)entity, (ModelPlayer)this.Field16, partialTicks);
        }
    }

    @Inject(method={"renderName"}, at={@At(value="HEAD")}, cancellable=true)
    private void Method38(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        if (Class511.Field1342) {
            ci.cancel();
            return;
        }
        Class139 event = new Class139(entity);
        Zenith.INSTANCE.getEventManager().dispatchEvent(event);
        if (event.Method1234()) {
            ci.cancel();
        }
    }*/


    /*@Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"), cancellable = true)
    private void renderModel(EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        RenderEntityModelEvent renderLivingEntityEvent = new RenderEntityModelEvent(mainModel, entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        Zenith.INSTANCE.getEventManager().dispatchEvent(renderLivingEntityEvent);

        if (renderLivingEntityEvent.isCanceled())
            info.cancel();
    }

    @Redirect(method={"renderLayers"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"))
    public void onRenderLayersDoLayers(LayerRenderer<EntityLivingBase> layer, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        RenderEntityLayerEvent event = new RenderEntityLayerEvent(entity, layer);
        Zenith.INSTANCE.getEventManager().dispatchEvent(event);
        if (!event.isCanceled()) {
            layer.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
        }
    }*/
}

