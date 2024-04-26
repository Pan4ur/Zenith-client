package me.gopro336.zenith.feature.toggleable.Combat;

import me.gopro336.zenith.api.util.interaction.InteractionUtil;
import me.gopro336.zenith.api.util.time.Timer;
import me.gopro336.zenith.asm.mixin.mixins.accessor.IEntityPlayerSP;
import me.gopro336.zenith.event.player.StopUsingItemEvent;
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "SelfBow", description = "Shoots yourself", category = Category.COMBAT)
public class Quiver extends Feature {
    public final Property<Boolean> speed = new Property<>(this, "Swiftness", "", false);
    public final Property<Boolean> strength = new Property<>(this, "Strength", "", false);
    public final Property<Boolean> toggelable = new Property<>(this, "Toggelable", "", false);
    public final Property<Boolean> autoSwitch = new Property<>(this, "AutoSwitch", "", false);
    public final Property<Boolean> rearrange = new Property<>(this, "Rearrange", "", false);
    public final Property<Boolean> noGapSwitch = new Property<>(this, "NoGapSwitch", "", false);
    public final NumberProperty<Integer> health = new NumberProperty<>(this, "MinHealth", "", 0, 20, 36);

    private Timer timer = new Timer();

    private boolean cancelStopUsingItem = false;

    @Listener//(priority = 98)
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        if (event.isCanceled() || !InteractionUtil.canPlaceNormally()) return;

        if (!timer.hasPassed(2500)) return;

        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() < health.getValue()) return;

        if (noGapSwitch.getValue() && mc.player.getActiveItemStack().getItem() instanceof ItemFood) return;

        if (strength.getValue() && !mc.player.isPotionActive(MobEffects.STRENGTH)) {
            if (isFirstAmmoValid("Arrow of Strength")) {
                shootBow(event);
            } else if (toggelable.getValue()) {
                toggle();
            }
        }

        if (speed.getValue() && !mc.player.isPotionActive(MobEffects.SPEED)) {
            if (isFirstAmmoValid("Arrow of Swiftness")) {
                shootBow(event);
            } else if (toggelable.getValue()) {
                toggle();
            }
        }
    }

    @Listener
    public void onStopUsingItem(StopUsingItemEvent event) {
        if (cancelStopUsingItem) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        cancelStopUsingItem = false;
    }

    private void shootBow(UpdateWalkingPlayerEvent event) {
        if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, -90, mc.player.onGround));
            ((IEntityPlayerSP) mc.player).setLastReportedYaw(0);
            ((IEntityPlayerSP) mc.player).setLastReportedPitch(-90);
            if (mc.player.getItemInUseMaxCount() >= 3) {
                cancelStopUsingItem = false;
                mc.playerController.onStoppedUsingItem(mc.player);
                if (toggelable.getValue()) {
                    toggle();
                }
                timer.reset();
            } else if (mc.player.getItemInUseMaxCount() == 0) {
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                cancelStopUsingItem = true;
            }
        } else if (autoSwitch.getValue()) {
            int bowSlot = getBowSlot();
            if (bowSlot != -1 && bowSlot != mc.player.inventory.currentItem) {
                mc.player.inventory.currentItem = bowSlot;
                mc.playerController.updateController();
            }
        }
    }

    public int getBowSlot() {
        int bowSlot = -1;

        if (mc.player.getHeldItemMainhand().getItem() == Items.BOW) {
            bowSlot = Feature.mc.player.inventory.currentItem;
        }


        if (bowSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.BOW) {
                    bowSlot = l;
                    break;
                }
            }
        }

        return bowSlot;
    }

    private boolean isFirstAmmoValid(String type) {
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                boolean matches = itemStack.getDisplayName().equalsIgnoreCase(type);
                if (matches) {
                    return true;
                } else if (rearrange.getValue()) {
                    return rearrangeArrow(i, type);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean rearrangeArrow(int fakeSlot, String type){
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                if (itemStack.getDisplayName().equalsIgnoreCase(type)) {
                    mc.playerController.windowClick(0, fakeSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, fakeSlot, 0, ClickType.PICKUP, mc.player);
                    return true;
                }
            }
        }
        return false;
    }
}
