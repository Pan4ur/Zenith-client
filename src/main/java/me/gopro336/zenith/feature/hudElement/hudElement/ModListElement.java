package me.gopro336.zenith.feature.hudElement.hudElement;

import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;

@AnnotationHelper(name = "ModList", category = Category.HUD)
public class ModListElement extends Element {
/*
    private Rainbow rainbow = new Rainbow();
    String comp;

    public static Value<Boolean> corner;
    public static Value<Boolean> animation;
    public static Value<String> type;
    public static Value<Boolean> alphab;
    public static Value<Boolean> box;
    public static Value<String> boxMode;
    public static Value<String> mode;
    public static Value<String> order;
    public static Value<String> rlist;
    public static Value<Integer> hudAlpha;
    public static Value<Integer> rainbowspeed;
    public static Value<Integer> rspeed;
    public static Value<Integer> rsaturation;
    public static Value<Integer> rlightness;
    public static Value<String> prefix;
    public static Value<String> suffix;
    public static Value<String> categoryProfile;
    
    public ModList() {

        setW(80);
        setH(80);

        corner = register(new Value<>("List In Corner", this, false));
        animation = register(new Value<>("Animation", this, false));
        type = register(new Value<>("Type", this, "Both", new String[]{"Both", "Enable", "Disable"}));
        alphab = register(new Value<>("Alphabetical", this, false));
        box = register(new Value<>("Boxes", this, false));
        boxMode = register(new Value<>("Box Mode", this, "Tag", new String[]{
                "Black", "Tag", "Outline"
        }));
        prefix = register(new Value<>("Prefix", this, "None", new String[]{
                "None", ">", ")", "]", "}", ">(space)", "->", "-", "=", "<", "(", "[", "{"
        }));
        suffix = register(new Value<>("Suffix", this, "None", new String[]{
                "None", ">", ")", "]", "}", "(space)<", "<-", "-", "=", "<", "(", "[", "{"
        }));
        mode = register(new Value<>("Aligned", this, "Left", new java.util.ArrayList<>(
                Arrays.asList("Left", "Right")
        )));
        order = register(new Value<>("Ordering", this, "Up", new java.util.ArrayList<>(
                Arrays.asList("Up", "Down")
        )));
        rlist = register(new Value<>("Color Mode", this, "ClickGui", new String[]{
                "ClickGui", "Rainbow", "Category"
        }));
        categoryProfile = register(new Value<>("Category Mode", this, "Xulu", new String[]{
                "Xulu", "Impact", "DotGod"
        }));
        rainbowspeed = register(new Value<>("Rainbow Speed", this, 5, 1, 100));
        hudAlpha = register(new Value<>("ListAlpha", this, 0, 255, 255));
        rspeed = register(new Value<>("Rainbow Size", this, 2, 0, 20));
        rsaturation = register(new Value<>("Rainbow Sat.", this, 112, 0, 255));
        rlightness = register(new Value<>("Rainbow Light.", this, 255, 0, 255));
    }

    public static char SECTIONSIGN() {
        return '\u00A7';
    }

    @Override
    public void render()
    {
        ScaledResolution s = new ScaledResolution(mc);
        double yCount = 3;
        double right = s.getScaledWidth() - 3 - Zenith.hudEditor.windows.get(0).W;
        if (!corner.getValue()) {
            yCount = y + 1;
            right = x + 1;
        }
        rainbow.updateRainbow();
        List<Module> mods = Zenith.moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .filter(Module::isNotHUD)
                //.filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> FontUtil.getStringWidth(module.getName() + (module.getHudInfo() == null ? "" : SECTIONSIGN() + "7 [" + module.getHudInfo() + "]")) * -1))
                .collect(Collectors.toList());
        if (alphab.getValue()) {
            String[] names = mods.stream().map(Module::getName).toArray(String[]::new);
            int count = mods.size();
            String temp;
            for (int i = 0; i < count; i++) {
                for (int j = i + 1; j < count; j++) {
                    if (names[i].compareTo(names[j]) > 0) {
                        temp = names[i];
                        names[i] = names[j];
                        names[j] = temp;
                    }
                }
            }
            mods.clear();
            for (String modname : names) {
                try {
                    mods.add(Zenith.moduleManager.getModuleByName(modname));
                } catch (Exception e) {
                    //empty
                }
            }
        }
        boolean start = true;
        if (order.getValue().equalsIgnoreCase("Down")) {
            yCount += 69;
        }
        float hue = rainbow.hue;
        for (Module module : mods) {
            int rgb2 = Color.HSBtoRGB(hue, rsaturation.getValue() / 255f, rlightness.getValue() / 255f);
            switch (rlist.getValue()) {
                case "ClickGui":
                    rgb2 = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue()).getRGB();
                    break;
                case "Category":
                    rgb2 = getCategoryColor(module);
                    break;
            }
            String pre;
            String suf;
            switch (prefix.getValue()) {
                case "None":
                    pre = "";
                    break;
                case ">(space)":
                    pre = "> ";
                    break;
                default:
                    pre = prefix.getValue();
            }
            switch (suffix.getValue()) {
                case "None":
                    suf = "";
                    break;
                case "(space)<":
                    suf = " <";
                    break;
                default:
                    suf = suffix.getValue();
            }
            String display = pre + module.getName() + (module.getHudInfo() == null ? "" : SECTIONSIGN() + "7 [" + SECTIONSIGN() + "f" + module.getHudInfo() + SECTIONSIGN() + "7]" + ChatFormatting.RESET) + suf;
            double right3 = (mode.getValue().equalsIgnoreCase("Right") ? right - FontUtil.getStringWidth(display) + Zenith.hudEditor.windows.get(0).W - 3 : right);
            double width = FontUtil.getStringWidth(display);
            if (box.getValue()) {
                switch (boxMode.getValue()) {
                    case "Black":
                        Gui.drawRect((int) right3 - 1, (int) yCount - 1, (int) right3 + (int) width + 3, (int) yCount + (int) FontUtil.getFontHeight(), 0x55111111);
                        break;
                    case "Tag":
                        Gui.drawRect((int) right3 - 1, (int) yCount - 1, (int) right3 + (int) width + 3, (int) yCount + (int) FontUtil.getFontHeight(), 0x55111111);
                        Gui.drawRect((int) right3 - 1, (int) yCount - 1, (int) right3 + 1, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                        break;
                    case "Outline":
                        Gui.drawRect((int) right3 - 1, (int) yCount - 1, (int) right3 + (int) width + 3, (int) yCount + (int) FontUtil.getFontHeight(), 0x55111111);
                        RenderUtil.drawRectOutline((int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width + 4, (int) yCount + (int) FontUtil.getFontHeight(), (int) right3 - 1, (int) yCount - 1, (int) right3 + (int) width + 3, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                        if (mods.indexOf(module) == 0) {
                            RenderUtil.drawRectOutline((int) right3 - 2, (int) yCount - 2, (int) right3 + (int) width + 4, (int) yCount + (int) FontUtil.getFontHeight(), (int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width + 4, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                            if (mods.indexOf(module) + 1 < mods.size()) {
                                Module mod = mods.get(mods.indexOf(module) + 1);
                                String next = pre + mod.getName() + (mod.getHudInfo() == null ? "" : SECTIONSIGN() + "7 [" + SECTIONSIGN() + "f" + mod.getHudInfo() + SECTIONSIGN() + "7]" + ChatFormatting.RESET) + suf;
                                double nextWidth = FontUtil.getStringWidth(next);
                                RenderUtil.drawRectOutline((int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width - nextWidth - 1, (int) yCount + (int) FontUtil.getFontHeight() + 1, (int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width - nextWidth - 1, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                            }
                        } else {
                            if (mods.indexOf(module) + 1 == mods.size()) {
                                RenderUtil.drawRectOutline((int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width + 4, (int) yCount + (int) FontUtil.getFontHeight() + 1, (int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width + 4, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                            } else {
                                Module mod = mods.get(mods.indexOf(module) + 1);
                                String next = pre + mod.getName() + (mod.getHudInfo() == null ? "" : SECTIONSIGN() + "7 [" + SECTIONSIGN() + "f" + mod.getHudInfo() + SECTIONSIGN() + "7]" + ChatFormatting.RESET) + suf;
                                double nextWidth = FontUtil.getStringWidth(next);
                                RenderUtil.drawRectOutline((int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width - nextWidth - 1, (int) yCount + (int) FontUtil.getFontHeight() + 1, (int) right3 - 2, (int) yCount - 1, (int) right3 + (int) width - nextWidth - 1, (int) yCount + (int) FontUtil.getFontHeight(), rgb2);
                            }
                        }
                        break;
                }
                start = false;
            }
            FontUtil.drawStringWithShadow(display, right3 + 1, yCount, ColorUtils.changeAlpha(rgb2, hudAlpha.getValue()));
            /*if (!animationMap.containsKey(module)) {
                if (module.inAnimation.getValue() != Module.Animation.NONE) {
                    if (ModList.mode.getValue().equalsIgnoreCase("Right")) {
                        if (module.inAnimation.getValue() == Module.Animation.ENABLE) {
                            animationMap.put(module, new Triplet<>(right3 + width, yCount, new Pair<>(right3, rgb2)));
                        } else {
                            animationMap.put(module, new Triplet<>(right3, yCount, new Pair<>(right3 + width, rgb2)));
                        }
                    } else if (ModList.mode.getValue().equalsIgnoreCase("Left")) {
                        if (module.inAnimation.getValue() == Module.Animation.ENABLE) {
                            animationMap.put(module, new Triplet<>(right3 - width, yCount, new Pair<>(right3, rgb2)));
                        } else {
                            animationMap.put(module, new Triplet<>(right3, yCount, new Pair<>(right3 - width, rgb2)));
                        }
                    }
                }
            } else {
                animationMap.get(module).getThird().setValue(rgb2);
            }*/
            /*yCount += (order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
            double speed = rspeed.getValue();
            hue += (speed / 100);
        }/*
        /*for (Module m : animationMap.keySet()) {
            Triplet<Double, Double, Pair<Double, Integer>> triplet = animationMap.get(m);
            String pre;
            String suf;
            switch (ModList.prefix.getValue()) {
                case "None":
                    pre = "";
                    break;
                case ">(space)":
                    pre = "> ";
                    break;
                default:
                    pre = ModList.prefix.getValue();
            }
            switch (ModList.suffix.getValue()) {
                case "None":
                    suf = "";
                    break;
                case "(space)<":
                    suf = " <";
                    break;
                default:
                    suf = ModList.suffix.getValue();
            }
            String display = pre + m.getName() + (m.getHudInfo() == null ? "" : SECTIONSIGN() + "7 [" + SECTIONSIGN() + "f" + m.getHudInfo() + SECTIONSIGN() + "7]" + ChatFormatting.RESET) + suf;
            FontUtil.drawStringWithShadow(display, triplet.getFirst(), triplet.getSecond(), ColorUtils.changeAlpha(triplet.getThird().getValue(), ModList.hudAlpha.getValue()));
            if (!triplet.getFirst().equals(triplet.getThird().getKey())) {
                if (triplet.getFirst() > triplet.getThird().getKey()) {
                    if (ModList.mode.getValue().equalsIgnoreCase("Left")) {
                        triplet.setFirst(triplet.getThird().getKey());
                    }
                    triplet.setFirst(triplet.getFirst() - 1);
                }
                if (triplet.getFirst() < triplet.getThird().getKey()) {
                    if (ModList.mode.getValue().equalsIgnoreCase("Right")) {
                        triplet.setFirst(triplet.getThird().getKey());
                    }
                    triplet.setFirst(triplet.getFirst() + 1);
                }
            } else {
                m.inAnimation.setEnumValue("NONE");
                removal.add(m);
            }
        }
        removal.forEach(module -> {
            if (module.inAnimation.getValue() == Module.Animation.NONE) animationMap.remove(module);
        });*/
        //removal.clear();
    }

   /* public String getTitle(String in) {
        in = Character.toUpperCase(in.toLowerCase().charAt(0)) + in.toLowerCase().substring(1);
        return in;
    }

    private int getCategoryColor(Module m) {
        switch (categoryProfile.getValue()) {
            case "Xulu":
                switch (m.getCategory()) {
                    case CLIENT:
                        return new Color(0, 218, 242).getRGB();
                    case COMBAT:
                        return new Color(222, 57, 11).getRGB();
                    case MOVEMENT:
                        return new Color(189, 28, 173).getRGB();
                    case EXPLOIT:
                        return new Color(83, 219, 41).getRGB();
                    case RENDER:
                        return new Color(255, 242, 62).getRGB();
                    case MISC:
                        return new Color(255, 143, 15).getRGB();
                    case HUD:
                        return new Color(255, 0, 123).getRGB();
                }
            case "Impact":
                switch (m.getCategory()) {
                    case CLIENT:
                        return new Color(0, 218, 242).getRGB();
                    case COMBAT:
                        return new Color(229, 30, 16).getRGB();
                    case MOVEMENT:
                        return new Color(8, 116, 227).getRGB();
                    case EXPLOIT:
                        return new Color(43, 203, 55).getRGB();
                    case RENDER:
                        return new Color(227, 162, 50).getRGB();
                    case MISC:
                        return new Color(97, 30, 212).getRGB();
                    case HUD:
                        return new Color(255, 0, 123).getRGB();
                }
            case "DotGod":
                switch (m.getCategory()) {
                    case CLIENT:
                        return new Color(0, 218, 242).getRGB();
                    case COMBAT:
                        return new Color(39, 181, 171).getRGB();
                    case MOVEMENT:
                        return new Color(26, 84, 219).getRGB();
                    case EXPLOIT:
                        return new Color(219, 184, 190).getRGB();
                    case RENDER:
                        return new Color(169, 204, 83).getRGB();
                    case MISC:
                        return new Color(215, 214, 216).getRGB();
                    case HUD:
                        return new Color(255, 0, 123).getRGB();
                }
        }
        return -1;
    }

    public class Rainbow {
        public int rgb;
        public int a;
        public int r;
        public int g;
        public int b;
        float hue = 0.01f;

        public void updateRainbow() {
            rgb = Color.HSBtoRGB(hue, rsaturation.getValue() / 255f, rlightness.getValue() / 255f);
            a = (rgb >>> 24) & 0xFF;
            r = (rgb >>> 16) & 0xFF;
            g = (rgb >>> 8) & 0xFF;
            b = rgb & 0xFF;
            hue += rainbowspeed.getValue() / 10000f;
            if (hue > 1) hue -= 1;
        }
    }

}*/
