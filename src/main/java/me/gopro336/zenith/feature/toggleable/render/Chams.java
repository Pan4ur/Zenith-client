package me.gopro336.zenith.feature.toggleable.render;

/**
 * @author Gopro336
 * old chams
 */
public class Chams/* extends Module*/ {/*
    public static Chams INSTANCE = new Chams();

    public static Value<Boolean> crystalChams;
    //public static Value<Boolean> color;
    public static Value<Boolean> rainbow;
    public static Value<Boolean> renderModel;
    public static Value<Integer> red;
    public static Value<Integer> green;
    public static Value<Integer> blue;
    public static Value<Integer> alpha;
    public static Value<Integer> saturation;
    public static Value<Integer> brightness;
    public static Value<Integer> speed;
    public static Value<Float> lineWidth;
    public static Value<String> renderMode;

    public Chams() {
        super("Chams", "Chams", Category.RENDER);
        crystalChams = register(new Value<>("Crystals", this, true));
        //color = register(new Value<>("Color", this, true));
        rainbow = register(new Value<>("Rainbow", this, true)).setParentProperty(crystalChams);
        renderModel = register(new Value<>("RenderModel", this, false)).setParentProperty(crystalChams);
        red = register(new Value<>("Red", this, 0, 0, 255)).setParentProperty(crystalChams);
        green = register(new Value<>("Green", this, 255, 0, 255)).setParentProperty(crystalChams);
        blue = register(new Value<>("Blue", this, 255, 0, 255)).setParentProperty(crystalChams);
        alpha = register(new Value<>("Alpha", this, 70, 0, 255)).setParentProperty(crystalChams);
        saturation = register(new Value<>("Saturation", this, 39, 0, 255)).setParentProperty(crystalChams);
        brightness = register(new Value<>("Brightness", this, 255, 0, 255)).setParentProperty(crystalChams);
        speed = register(new Value<>("Speed", this, 2, 0, 10)).setParentProperty(crystalChams);
        lineWidth = register(new Value<>("Line", this, 2.0f, 0.1f, 4.0f)).setParentProperty(crystalChams);
        renderMode = register(new Value<>("Esp Mode", this, "FULL", new String[]{
                "FULL",
                "WIREFRAME",
                "SOLID"
        })).setParentProperty(crystalChams);
        setInstance();
    }

    public static Chams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (crystalChams.getValue()) {
            if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal)) {
                return;
            }
            if (renderMode.getValue().equals("WIREFRAME") || renderMode.getValue().equals("FULL")) {
                Color color = rainbow.getValue() ? new Color(RainbowUtil.rainbow(speed.getValue(), saturation.getValue(), brightness.getValue()).getRGB()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
                mc.gameSettings.fancyGraphics = false;
                mc.gameSettings.gammaSetting = 10000.0f;
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color(((float)color.getRed() / 255.0f), ((float)color.getGreen() / 255.0f), ((float)color.getBlue() / 255.0f), ((float)alpha.getValue() / 255.0f));
                GlStateManager.glLineWidth(lineWidth.getValue().floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }
    }*/
}
