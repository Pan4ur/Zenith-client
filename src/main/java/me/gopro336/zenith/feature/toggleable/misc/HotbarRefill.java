package me.gopro336.zenith.feature.toggleable.misc;

import me.gopro336.zenith.api.util.time.Timer;
import me.gopro336.zenith.event.world.UpdateEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AnnotationHelper(name = "HotbarRefill", description = "Automatically refills your hotbar", category = Category.MISC)
public class HotbarRefill extends Feature {

    private Property<Boolean> itemSaver = new Property<>(this, "ItemSaver", "", false);
    public NumberProperty<Integer> refillThreshold = new NumberProperty<>(this, "RefillThreshold", "", 1, 36, 64);
    public NumberProperty<Integer> delay = new NumberProperty<>(this, "Delay", "", 1, 1, 20);
    private Property<Boolean> crystals = new Property<>(this, "Crystals", "", true);
    private Property<Boolean> xp = new Property<>(this, "EXp", "", true);
    private Property<Boolean> food = new Property<>(this, "Food", "", true);
    private Property<Boolean> others = new Property<>(this, "Others", "", false);

    public ConcurrentHashMap<ItemStack, Integer> itemsToRefill = new ConcurrentHashMap<>();

    public static Timer moveTimer = new Timer();

    private int ticks = 0;

    @Listener
    public void onUpdate(UpdateEvent event) {

        if (mc.player == null || mc.world == null || mc.currentScreen instanceof GuiContainer || event.getPhase() == TickEvent.Phase.START) return;

        if (!moveTimer.hasPassed(350)) return;

        if (itemSaver.getValue()) {
            boolean itemSaved = false;
            EnumHand hands[] = EnumHand.values();
            for (int i = 0; i < hands.length; i++) {
                EnumHand hand = hands[i];
                ItemStack stack = mc.player.getHeldItem(hand);
                if (stack != null && stack.getItem() != null) {
                    Item item = stack.getItem();
                    if (stack.isItemStackDamageable() && stack.getItemDamage() == item.getMaxDamage(stack)) {
                        switch (hand) {
                            case MAIN_HAND: {
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, mc.player.inventory.currentItem + 36, 0, ClickType.QUICK_MOVE, mc.player);
                                itemSaved = true;
                                break;
                            }
                            case OFF_HAND: {
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 1, ClickType.QUICK_MOVE, mc.player);
                                itemSaved = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (itemSaved) {
                ticks = 0;
                return;
            }
        }
        
        if (ticks > delay.getValue() * 2) {
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
            findItemsToRefill();
            refillItems();
            ticks = 0;
        } else {
            ticks++;
        }

    }

    private void findItemsToRefill() {
        for (int i = 0; i <= 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            // If Slot is Empty continue
            if (stack.isEmpty() || stack.getItem() == Items.AIR) continue;

            // If Slot is not stackable continue
            if (!stack.isStackable()) continue;

            // If Slot is full continue
            if (stack.getCount() >= stack.getMaxStackSize()) continue;

            // If Slot is above threshold continue
            if (stack.getCount() >= refillThreshold.getValue()) continue;

            if (others.getValue() || (stack.getItem() instanceof ItemEndCrystal && crystals.getValue()) || (stack.getItem() instanceof ItemFood && food.getValue()) || (stack.getItem() instanceof ItemExpBottle && xp.getValue())) {
                itemsToRefill.put(stack, i);
            }

        }
    }

    private boolean isInventoryGood() {
        for (int i = 0; i < 36; i++) {
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                return true;
            }
        }
        return false;
    }

    private void refillItems() {
        for (Map.Entry<ItemStack, Integer> entry : itemsToRefill.entrySet()) {
            ItemStack stack = entry.getKey();
            int slotToRefill = entry.getValue();
            if(mc.player.inventory.getSlotFor(stack) == -1) {
                continue;
            }
            int refillSlot = -1;
            for (int i = 9; i <= 35; i++) {
                ItemStack refillStack = mc.player.inventory.getStackInSlot(i);
                if (refillStack.getItem().equals(stack.getItem())
                        && refillStack.getDisplayName().equals(stack.getDisplayName())
                        && refillStack.getItemDamage() == stack.getItemDamage()) {
                    refillSlot = i;
                    break;
                }
            }
            if(refillSlot != -1) {
                mc.playerController.windowClick(0, refillSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, slotToRefill < 9 ? slotToRefill + 36 : slotToRefill, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, refillSlot, 0, ClickType.PICKUP, mc.player);
                itemsToRefill.remove(stack);
            }
        }
    }

}