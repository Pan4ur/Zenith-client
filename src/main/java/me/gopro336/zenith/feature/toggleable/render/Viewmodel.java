package me.gopro336.zenith.feature.toggleable.render;
/**
 * old module, still needs to be re-implemented into the rewrite
 */
/*
import me.gopro336.zenith.event.player.TransformFirstPersonEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.Property.Value;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class Viewmodel extends Module {/*

    public static Value<Boolean> cancelEating;

    public final Value<Boolean> leftPosition = register(new Value<>("Left Position", this, true));
    public final Value<Double> leftX = register(new Value<>("Left X", this, 0.0, -2.0, 2.0)).setParentProperty(leftPosition);
    public final Value<Double> leftY = register(new Value<>("Left Y", this, 0.2, -2.0, 2.0)).setParentProperty(leftPosition);
    public final Value<Double> leftZ = register(new Value<>("Left Z", this, -1.2, -2.0, 2.0)).setParentProperty(leftPosition);

    public final Value<Boolean> rightPosition = register(new Value<>("Right Position", this, true));
    public final Value<Double> rightX = register(new Value<>("Right X", this, 0.0, -2.0, 2.0)).setParentProperty(rightPosition);
    public final Value<Double> rightY = register(new Value<>("Right Y", this, 0.2, -2.0, 2.0)).setParentProperty(rightPosition);
    public final Value<Double> rightZ = register(new Value<>("Right Z", this, -1.2, -2.0, 2.0)).setParentProperty(rightPosition);

    public final Value<Boolean> leftRotation = register(new Value<>("Left Rotation", this, true));
    public final Value<Integer> leftYaw = register(new Value<>("Yaw", this, 0, -100, 100)).setParentProperty(leftRotation);
    public final Value<Integer> leftPitch = register(new Value<>("Pitch", this, 0, -100, 100)).setParentProperty(leftRotation);
    public final Value<Integer> leftRoll = register(new Value<>("Roll", this, 0, -100, 100)).setParentProperty(leftRotation);

    public final Value<Boolean> rightRotation = register(new Value<>("Right Rotation", this, true));
    public final Value<Integer> rightYaw = register(new Value<>("Yaw", this, 0, -100, 100)).setParentProperty(rightRotation);
    public final Value<Integer> rightRoll = register(new Value<>("Roll", this, 0, -100, 100)).setParentProperty(rightRotation);
    public final Value<Integer> rightPitch = register(new Value<>("Pitch", this, 0, -100, 100)).setParentProperty(rightRotation);

    public Viewmodel() {
        super("ViewModel", "", Category.RENDER);
        cancelEating = register(new Value<>("CancelEating", this, true));
    }

    @SubscribeEvent
    public void onTransformSideFirstPerson(TransformFirstPersonEvent.Pre event) {
        if (nullCheck()) return;

        if (leftPosition.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(leftX.getValue(), leftY.getValue(), leftZ.getValue());
        }

        if (rightPosition.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.getValue(), rightY.getValue(), rightZ.getValue());
        }
    }

    @SubscribeEvent
    public void onTransFormPost(TransformFirstPersonEvent.Post event) {
        if (nullCheck()) return;

        if (leftRotation.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.rotate(leftYaw.getValue(),0,1,0);
            GlStateManager.rotate(leftPitch.getValue(),1,0,0);
            GlStateManager.rotate(leftRoll.getValue(),0,0,1);
        }

        if (rightRotation.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.rotate(rightYaw.getValue(),0,1,0);
            GlStateManager.rotate(rightPitch.getValue(),1,0,0);
            GlStateManager.rotate(rightRoll.getValue(),0,0,1);
        }
    }*/
//}