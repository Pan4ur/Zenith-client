package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.api.util.combat.CrystalUtils;
import me.gopro336.zenith.api.util.time.Timer;
import me.gopro336.zenith.event.world.PreTickEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.toggleable.misc.HotbarRefill;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.*;
import java.util.stream.Collectors;

@AnnotationHelper(name = "Offhand", category = Category.COMBAT, description = "Makes use of your offhand")
public class Offhand extends Feature {

    public Property<Boolean> totem = new Property<>(this, "Totem", "", true);
    public Property<Boolean> gapple = new Property<>(this, "Gapple", "", true);
    public Property<Boolean> crystal = new Property<>(this, "Crystal", "", true);
    public NumberProperty<Float> delay = new NumberProperty<>(this, "Delay", "",0.0f, 0.0f, 5.0f);
    public Property<Boolean> hotbarTotem = new Property<>(this, "HotbarTotem", "", false);
    public NumberProperty<Float> totemHealthThreshold = new NumberProperty<>(this, "TotemHealth", "", 0.0f, 5.0f, 36.0f);
    public Property<Boolean> rightClick = new Property<>(this, "RightClickGap", "", true, v-> gapple.getValue());
    public Property<CrystalCheck> crystalCheck = new Property<>(this, "CrystalCheck", "", CrystalCheck.DAMAGE);
    public NumberProperty<Float> crystalRange = new NumberProperty<>(this, "CrystalRange", "", 1.0f, 10.0f, 15.0f, v-> crystalCheck.getValue() != CrystalCheck.NONE);
    public Property<Boolean> fallCheck = new Property<>(this, "FallCheck", "", true);
    public NumberProperty<Float> fallDist = new NumberProperty<>(this, "FallDist", "", 0.0f, 15.0f, 50.0f, v-> fallCheck.getValue());
    public Property<Boolean> totemOnElytra = new Property<>(this, "TotemOnElytra", "", true);
    public Property<Boolean> extraSafe = new Property<>(this, "ExtraSafe", "", false);
    public Property<Boolean> clearAfter = new Property<>(this, "ClearAfter", "", true);
    public Property<Boolean> hard = new Property<>(this, "Hard", "",false);
    public Property<Boolean> notFromHotbar = new Property<>(this, "NotFromHotbar", "", true);
    public Property<Default> defaultItem = new Property<>(this, "DefaultItem", "", Default.TOTEM);

    private final Queue<Integer> clickQueue = new LinkedList<>();

    private Timer timer = new Timer();

    private enum CrystalCheck {
        NONE,
        DAMAGE,
        RANGE
    }

    private enum Default {
        TOTEM(Items.TOTEM_OF_UNDYING),
        CRYSTAL(Items.END_CRYSTAL),
        GAPPLE(Items.GOLDEN_APPLE),
        AIR(Items.AIR);

        public Item item;

        Default(Item item) {
            this.item = item;
        }
    }

    @Listener
    public void onUpdate(PreTickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (!(mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof GuiInventory)) {
            if (!clickQueue.isEmpty()) {
                if (!timer.hasPassed(delay.getValue() * 100F)) return;
                int slot = clickQueue.poll();
                try {
                    HotbarRefill.moveTimer.reset();
                    timer.reset();
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (!mc.player.inventory.getItemStack().isEmpty()) {
                    int index = 44;
                    while (index >= 9) {
                        if (mc.player.inventoryContainer.getSlot(index).getStack().isEmpty()) {
                            mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, mc.player);
                            return;
                        }
                        index--;
                    }
                }

                if (totem.getValue()) {
                    if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= totemHealthThreshold.getValue() || (totemOnElytra.getValue() && mc.player.isElytraFlying()) || (fallCheck.getValue() && mc.player.fallDistance >= fallDist.getValue() && !mc.player.isElytraFlying())) {
                        putItemIntoOffhand(Items.TOTEM_OF_UNDYING);
                        return;
                    } else if (crystalCheck.getValue() == CrystalCheck.RANGE) {
                        EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream()
                                .filter(e -> (e instanceof EntityEnderCrystal && mc.player.getDistance(e) <= crystalRange.getValue()))
                                .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                                .orElse(null);

                        if (crystal != null) {
                            putItemIntoOffhand(Items.TOTEM_OF_UNDYING);
                            return;
                        }
                    } else if (crystalCheck.getValue() == CrystalCheck.DAMAGE) {
                        float damage = 0.0f;

                        List<Entity> crystalsInRange = mc.world.loadedEntityList.stream()
                                .filter(e -> e instanceof EntityEnderCrystal)
                                .filter(e -> mc.player.getDistance(e) <= crystalRange.getValue())
                                .collect(Collectors.toList());

                        for (Entity entity : crystalsInRange) {
                            damage += CrystalUtils.calculateDamage((EntityEnderCrystal) entity, mc.player);
                        }

                        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() - damage <= totemHealthThreshold.getValue()) {
                            putItemIntoOffhand(Items.TOTEM_OF_UNDYING);
                            return;
                        }
                    }

                    if (extraSafe.getValue()) {
                        if (crystalCheck()) {
                            putItemIntoOffhand(Items.TOTEM_OF_UNDYING);
                            return;
                        }
                    }
                }

                if (gapple.getValue()
                        && isSword(mc.player.getHeldItemMainhand().getItem())) {

                    if (rightClick.getValue()
                            && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
                        if (clearAfter.getValue()) {
                            putItemIntoOffhand(defaultItem.getValue().item);
                        }
                        return;
                    }

                    putItemIntoOffhand(Items.GOLDEN_APPLE);
                    return;
                }

                if (crystal.getValue()) {
                    /*if (FeatureManager.getFeatureByClass(AutoCrystal.class).isEnabled()) {
                        putItemIntoOffhand(Items.END_CRYSTAL);
                        return;
                    } else */if (clearAfter.getValue()) {
                        putItemIntoOffhand(defaultItem.getValue().item);
                        return;
                    }
                }

                if (hard.getValue()) {
                    putItemIntoOffhand(defaultItem.getValue().item);
                }
            }
        }
    }

    private boolean isSword(Item item) {
        return item == Items.DIAMOND_SWORD || item == Items.IRON_SWORD || item == Items.GOLDEN_SWORD || item == Items.STONE_SWORD || item == Items.WOODEN_SWORD;
    }

    private int findItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = notFromHotbar.getValue() ? 9 : 0; i < 36; i++) {

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() == item) {
                itemSlot = i;
                break;
            }

        }
        return itemSlot;
    }

    private void putItemIntoOffhand(Item item) {
        if (mc.player.getHeldItemOffhand().getItem() == item) return;
        int slot = findItemSlot(item);
        if (hotbarTotem.getValue() && item == Items.TOTEM_OF_UNDYING) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.inventory.mainInventory.get(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    if (mc.player.inventory.currentItem != i) {
                        mc.player.inventory.currentItem = i;
                    }
                    return;
                }
            }
        }
        if (slot != -1) {
            if (delay.getValue() > 0F) {
                if (timer.hasPassed(delay.getValue() * 100F)) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
                    timer.reset();
                } else {
                    clickQueue.add(slot < 9 ? slot + 36 : slot);
                }

                clickQueue.add(45);
                clickQueue.add(slot < 9 ? slot + 36 : slot);
            } else {
                timer.reset();
                HotbarRefill.moveTimer.reset();
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
                try {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    private boolean crystalCheck() {
        float cumDmg = 0;
        ArrayList<Float> damageValues = new ArrayList<>();
        damageValues.add(calculateDamageAABB(mc.player.getPosition().add(1, 0, 0)));
        damageValues.add(calculateDamageAABB(mc.player.getPosition().add(-1, 0, 0)));
        damageValues.add(calculateDamageAABB(mc.player.getPosition().add(0, 0, 1)));
        damageValues.add(calculateDamageAABB(mc.player.getPosition().add(0, 0, -1)));
        damageValues.add(calculateDamageAABB(mc.player.getPosition()));
        for (float damage : damageValues) {
            cumDmg += damage;
            if ((((mc.player.getHealth() + mc.player.getAbsorptionAmount())) - damage) <= totemHealthThreshold.getValue()) {
                return true;
            }
        }

        if ((((mc.player.getHealth() + mc.player.getAbsorptionAmount())) - cumDmg) <= totemHealthThreshold.getValue()) {
            return true;
        }

        return false;
    }

    private float calculateDamageAABB(BlockPos pos){
        List<Entity> crystalsInAABB =  mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream()
                .filter(e -> e instanceof EntityEnderCrystal)
                .collect(Collectors.toList());
        float totalDamage = 0;
        for (Entity crystal : crystalsInAABB) {
            totalDamage += CrystalUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, mc.player);
        }
        return totalDamage;
    }
}
