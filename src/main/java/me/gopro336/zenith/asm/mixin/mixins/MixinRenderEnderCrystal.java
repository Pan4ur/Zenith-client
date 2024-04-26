package me.gopro336.zenith.asm.mixin.mixins;

import me.gopro336.zenith.feature.toggleable.render.NewChams;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderEnderCrystal.class, priority = Integer.MAX_VALUE - 1)
//public abstract class MixinRenderEnderCrystal extends Render{
public abstract class MixinRenderEnderCrystal extends Render<EntityEnderCrystal> {
    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }


    /*private static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Final
    @Shadow
    private ModelBase modelEnderCrystal;

    @Final
    @Shadow
    private ModelBase modelEnderCrystalNoBase;*/

    //I dont think these work
    /*@Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void doRender(ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderCrystalEvent.RenderCrystalPreEvent renderCrystalEvent = new RenderCrystalEvent.RenderCrystalPreEvent(modelBase, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Zenith.INSTANCE.getEventManager().dispatchEvent(renderCrystalEvent);

        if (!renderCrystalEvent.isCanceled())
            modelBase.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        CrystalTextureEvent crystalTextureEvent = new CrystalTextureEvent();
        Zenith.INSTANCE.getEventManager().dispatchEvent(crystalTextureEvent);
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V", at = @At("RETURN"), cancellable = true)
    public void doRender(EntityEnderCrystal entityEnderCrystal, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        RenderCrystalEvent.RenderCrystalPostEvent renderCrystalEvent = new RenderCrystalEvent.RenderCrystalPostEvent(modelEnderCrystal, modelEnderCrystalNoBase, entityEnderCrystal, x, y, z, entityYaw, partialTicks);
        Zenith.INSTANCE.getEventManager().dispatchEvent(renderCrystalEvent);

        if (renderCrystalEvent.isCanceled())
            info.cancel();
    }*/

    /*@Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (Chams2.crystalChams.getValue()) {
            //GlStateManager.scale((float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue(), (float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue(), (float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue());
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
        } else {
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
        }
        if (Chams2.crystalChams.getValue() && !Chams2.renderModel.getValue()) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        /*if (Chams2.crystalChams.getValue() && Chams2.renderMode.getValue().equals("WIREFRAME") || Chams2.renderMode.getValue().equals("FULL")) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Chams2.INSTANCE.onRenderModel(event);
        }*/
        //if (Chams2.crystalChams.getValue() && (Chams2.renderMode.getValue().equals("SOLID") || Chams2.renderMode.getValue().equals("FULL"))) {
        /*if (Chams2.crystalChams.getValue()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            if (Chams2.rainbow.getValue()) {
                Color rainbowColor = new Color(RainbowUtil.rainbow(Chams2.speed.getValue(), Chams2.saturation.getValue(), Chams2.brightness.getValue()).getRGB());
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                GL11.glColor4f(((float)rainbowColor.getRed() / 255.0f), ((float)rainbowColor.getGreen() / 255.0f), ((float)rainbowColor.getBlue() / 255.0f), ((float)Chams2.alpha.getValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            } else {
                Color visibleColor;
                Color color = visibleColor = new Color(Chams2.red.getValue(), Chams2.green.getValue(),Chams2.blue.getValue(), Chams2.alpha.getValue());
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                GL11.glColor4f(((float)visibleColor.getRed() / 255.0f), ((float)visibleColor.getGreen() / 255.0f), ((float)visibleColor.getBlue() / 255.0f), ((float)Chams2.alpha.getValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }*/
        /*if (Chams2.crystalChams.getValue() && Chams2.glint.getValue()) {
            //float f = (float)p_188364_1_.ticksExisted + p_188364_5_;
            float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;//taken from itemGlint

            bindTexture(ENCHANTED_ITEM_GLINT_RES);

            //Zenith.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            float f1 = 0.5F;
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

            for(int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                float f2 = 0.76F;
                GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f3 = 0.33333334F;
                GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
                GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);

                //GlStateManager.translate(-f1, 0.0F, 0.0F);//taken from itemGlint

                GlStateManager.translate(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);

                GlStateManager.matrixMode(5888);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }*/



        /*if (Chams.crystalChams.getValue()) {
            //GlStateManager.scale((float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue(), (float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue(), (float)CrystalChams.INSTANCE.crystalScale.getValue().floatValue());
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
        } else {
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
        }*/
   // }



    /*@Final
    @Shadow
    private ModelBase modelEnderCrystal;

    @Final
    @Shadow
    private ModelBase modelEnderCrystalNoBase;*/


    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;

    /*@Shadow
    public abstract void Method61(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9);*/

    @Redirect(method={"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void doRender(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (NewChams.INSTANCE.isEnabled() /*&& !Class511.Field1342 */&& ((Boolean)NewChams.Field2119.getValue()).booleanValue()) {
            if (((Boolean)NewChams.Field2119.getValue()).booleanValue() && ((Boolean)NewChams.Field2120.getValue()).booleanValue()) {
                GL11.glScalef((float)((Float)NewChams.Field2135.getValue()).floatValue(), (float)((Float)NewChams.Field2135.getValue()).floatValue(), (float)((Float)NewChams.Field2135.getValue()).floatValue());
                if (!((Boolean)NewChams.Field2121.getValue()).booleanValue()) {
                    GL11.glPushAttrib((int)1048575);
                    GL11.glDepthMask((boolean)false);
                    GL11.glDisable((int)2929);
                }
                modelBase.render(entityIn, limbSwing, limbSwingAmount * ((Float)NewChams.Field2136.getValue()).floatValue(), ageInTicks * ((Float)NewChams.Field2137.getValue()).floatValue(), netHeadYaw, headPitch, scale);
                if (!((Boolean)NewChams.Field2121.getValue()).booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glPopAttrib();
                }
                GL11.glScalef((float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()), (float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()), (float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()));
            } else if (!((Boolean)NewChams.Field2119.getValue()).booleanValue()) {
                modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        } else if (NewChams.INSTANCE.isEnabled() /*&& Class511.Field1342 */&& ((Boolean)NewChams.Field2119.getValue()).booleanValue()) {
            GL11.glScalef((float)((Float)NewChams.Field2135.getValue()).floatValue(), (float)((Float)NewChams.Field2135.getValue()).floatValue(), (float)((Float)NewChams.Field2135.getValue()).floatValue());
            modelBase.render(entityIn, limbSwing, limbSwingAmount * ((Float)NewChams.Field2136.getValue()).floatValue(), ageInTicks * ((Float)NewChams.Field2137.getValue()).floatValue(), netHeadYaw, headPitch, scale);
            GL11.glScalef((float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()), (float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()), (float)(1.0f / ((Float)NewChams.Field2135.getValue()).floatValue()));
        } else {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    /*@Inject(method={"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at={@At(value="RETURN")}, cancellable=true)
    public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (NewChams.INSTANCE.isEnabled() && /*!Class511.Field1342 &&*/ //((Boolean) NewChams.Field2119.getValue()).booleanValue()) {
            /*float f4;
            float f3;
            if (((Boolean) NewChams.Field2127.getValue()).booleanValue()) {
                f3 = (float) entity.innerRotation + partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.translate((double) x, (double) y, (double) z);
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(ENDER_CRYSTAL_TEXTURES);
                f4 = MathHelper.sin((float) (f3 * 0.2f)) / 2.0f + 0.5f;
                f4 += f4 * f4;
                GL11.glPushAttrib((int) 1048575);
                GL11.glPolygonMode((int) 1032, (int) 6914);
                GL11.glDisable((int) 3553);
                if (((Boolean) NewChams.Field2129.getValue()).booleanValue()) {
                    GL11.glEnable((int) 2896);
                } else {
                    GL11.glDisable((int) 2896);
                }
                GL11.glDisable((int) 2929);
                GL11.glEnable((int) 2848);
                GL11.glEnable((int) 3042);
                GL11.glBlendFunc((int) 770, (int) 771);
                GL11.glColor4f((float) ((float) Method769(NewChams.Field2130.getValue().getRGB()) / 255.0f), (float) ((float) Method770(NewChams.Field2130.getValue().getRGB()) / 255.0f), (float) ((float) Method779(NewChams.Field2130.getValue().getRGB()) / 255.0f), (float) ((float) Method782(NewChams.Field2130.getValue().getRGB()) / 255.0f));
                GL11.glScalef((float) ((Float) NewChams.Field2135.getValue()).floatValue(), (float) ((Float) NewChams.Field2135.getValue()).floatValue(), (float) ((Float) NewChams.Field2135.getValue()).floatValue());
                if (((Boolean) NewChams.Field2128.getValue()).booleanValue()) {
                    GL11.glDepthMask((boolean) true);
                    GL11.glEnable((int) 2929);
                }
                if (entity.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                } else {
                    this.modelEnderCrystalNoBase.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                }
                if (((Boolean) NewChams.Field2128.getValue()).booleanValue()) {
                    GL11.glDisable((int) 2929);
                    GL11.glDepthMask((boolean) false);
                }
                GL11.glScalef((float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()));
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if (((Boolean) NewChams.Field2131.getValue()).booleanValue()) {
                f3 = (float) entity.innerRotation + partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.translate((double) x, (double) y, (double) z);
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(ENDER_CRYSTAL_TEXTURES);
                f4 = MathHelper.sin((float) (f3 * 0.2f)) / 2.0f + 0.5f;
                f4 += f4 * f4;
                GL11.glPushAttrib((int) 1048575);
                GL11.glPolygonMode((int) 1032, (int) 6913);
                GL11.glDisable((int) 3553);
                GL11.glDisable((int) 2896);
                GL11.glDisable((int) 2929);
                GL11.glEnable((int) 2848);
                GL11.glEnable((int) 3042);
                GL11.glBlendFunc((int) 770, (int) 771);
                GL11.glLineWidth((float) ((Float) NewChams.Field2133.getValue()).floatValue());
                GL11.glColor4f((float) ((float) Method769(NewChams.Field2134.getValue().getRGB()) / 255.0f), (float) ((float) Method770(NewChams.Field2134.getValue().getRGB()) / 255.0f), (float) ((float) Method779(NewChams.Field2134.getValue().getRGB()) / 255.0f), (float) ((float) Method782(NewChams.Field2134.getValue().getRGB()) / 255.0f));
                GL11.glScalef((float) ((Float) NewChams.Field2135.getValue()).floatValue(), (float) ((Float) NewChams.Field2135.getValue()).floatValue(), (float) ((Float) NewChams.Field2135.getValue()).floatValue());
                if (((Boolean) NewChams.Field2132.getValue()).booleanValue()) {
                    GL11.glDepthMask((boolean) true);
                    GL11.glEnable((int) 2929);
                }
                if (entity.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                } else {
                    this.modelEnderCrystalNoBase.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                }
                if (((Boolean) NewChams.Field2132.getValue()).booleanValue()) {
                    GL11.glDisable((int) 2929);
                    GL11.glDepthMask((boolean) false);
                }
                GL11.glScalef((float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()));
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if (NewChams.Field2122.getValue() != NewChams.Class495.NONE) {
                f3 = (float) entity.innerRotation + partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.translate((double) x, (double) y, (double) z);
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(NewChams.Field2122.getValue() == NewChams.Class495.CUSTOM ? NewChams.Field2146 : NewChams.Field2145);
                f4 = MathHelper.sin((float) (f3 * 0.2f)) / 2.0f + 0.5f;
                f4 += f4 * f4;
                GL11.glPushAttrib((int) 1048575);
                GL11.glPolygonMode((int) 1032, (int) 6914);
                GL11.glDisable((int) 2896);
                GL11.glDisable((int) 2929);
                GL11.glEnable((int) 3042);
                GL11.glColor4f((float) ((float) Method769(NewChams.Field2126.getValue().getRGB()) / 255.0f), (float) ((float) Method770(NewChams.Field2126.getValue().getRGB()) / 255.0f), (float) ((float) Method779(NewChams.Field2126.getValue().getRGB()) / 255.0f), (float) ((float) Method782(NewChams.Field2126.getValue().getRGB()) / 255.0f));
                GL11.glScalef((float) (Float) NewChams.Field2135.getValue(), (float) (Float) NewChams.Field2135.getValue(), (float) NewChams.Field2135.getValue().floatValue());
                GlStateManager.blendFunc((GlStateManager.SourceFactor) GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor) GlStateManager.DestFactor.ONE);
                for (int i = 0; i < 2; ++i) {
                    GlStateManager.matrixMode((int) 5890);
                    GlStateManager.loadIdentity();
                    float tScale = 0.33333334f * (Float) NewChams.Field2125.getValue();
                    GlStateManager.scale((float) tScale, (float) tScale, (float) tScale);
                    GlStateManager.rotate((float) (30.0f - (float) i * 60.0f), (float) 0.0f, (float) 0.0f, (float) 1.0f);
                    GlStateManager.translate((float) 0.0f, (float) (((float) entity.ticksExisted + partialTicks) * (0.001f + (float) i * 0.003f) * ((Float) NewChams.Field2124.getValue()).floatValue()), (float) 0.0f);
                    GlStateManager.matrixMode((int) 5888);
                    if ((Boolean) NewChams.Field2123.getValue()) {
                        GL11.glDepthMask((boolean) true);
                        GL11.glEnable((int) 2929);
                    }
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                    } else {
                        this.modelEnderCrystalNoBase.render((Entity) entity, 0.0f, f3 * 3.0f * ((Float) NewChams.Field2136.getValue()).floatValue(), f4 * 0.2f * ((Float) NewChams.Field2137.getValue()).floatValue(), 0.0f, 0.0f, 0.0625f);
                    }
                    if (!((Boolean) NewChams.Field2123.getValue()).booleanValue()) continue;
                    GL11.glDisable((int) 2929);
                    GL11.glDepthMask((boolean) false);
                }
                GlStateManager.matrixMode((int) 5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode((int) 5888);
                GlStateManager.blendFunc((GlStateManager.SourceFactor) GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor) GlStateManager.DestFactor.ZERO);
                GL11.glScalef((float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()), (float) (1.0f / ((Float) NewChams.Field2135.getValue()).floatValue()));
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }

    }*/
}
