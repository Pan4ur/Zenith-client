package me.gopro336.zenith.feature.toggleable.movement;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.client.PlayerUtils;
import me.gopro336.zenith.api.util.time.Timer;
import me.gopro336.zenith.asm.mixin.imixin.ICPacketPlayer;
import me.gopro336.zenith.event.EventStageable;
import me.gopro336.zenith.event.entity.PlayerUseItemEvent;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.event.player.MoveEvent;
import me.gopro336.zenith.event.player.PlayerUpdateEvent;
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent;
import me.gopro336.zenith.event.world.UpdateEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@AnnotationHelper(name = "Speed", description = "Makes you move faster", category = Category.MOVEMENT)
public class Speed extends Feature {

    public NumberProperty<Double> speed = new NumberProperty<>(this, "Speed", "",  0.0, 1.0, 10.0);
    public Property<Boolean> useTimer = new Property<>(this, "UseTimer", "", true);
    public NumberProperty<Float> timerFactor = new NumberProperty<>(this, useTimer, "Factor", "", 0.0f, 1.0f, 10.0f);//.visibleIf(this.useTimer::getValue);
    public Property<Mode> mode = new Property<>(this, "Mode", "", Mode.STRAFE);
    public Property<Boolean> bypass = new Property<>(this, "Bypass", "", false, v-> getMode() == Mode.ONGROUND);//.visibleIf(this::Method396);
    public Property<Boolean> hypixel = new Property<>(this, "Hypixel", "", false, v-> getMode() == Mode.STRAFEOLD);//.visibleIf(this::Method393);
    public Property<Boolean> allowEat = new Property<>(this, "AllowEat", "", false, v-> getMode() == Mode.STRAFESTRICT);//.visibleIf(this::Method539);
    public Property<Boolean> strict = new Property<>(this, "Strict", "", false, v-> getMode() == Mode.STRAFE);//.visibleIf(this::Method538);
    public Property<Boolean> disableOnSneak = new Property<>(this, "DisableOnSneak", "", false);
    public Property<Boolean> forceSprint = new Property<>(this, "ForceSprint", "", false);
    public Property<Boolean> boost = new Property<>(this, "Boost", "", false, v-> getMode() == Mode.SMALLHOP || getMode() == Mode.STRAFE || getMode() == Mode.STRAFEOLD || getMode() == Mode.STRAFESTRICT);//.visibleIf(this::Method535);
    public NumberProperty<Double> boostFactor = new NumberProperty<>(this, "BoostFactor", "", 0.0, 1.0, 10.0, v-> getMode() == Mode.SMALLHOP && boost.getValue());//.visibleIf(this::Method519);


    private enum Mode {
        STRAFE, STRAFESTRICT, ONGROUND, LOWHOP, SMALLHOP, TP, STRAFEOLD
    }

    private int strafeStage = 1;

    public int hopStage;

    private double ncpPrevMotion = 0.0D;

    private double horizontal;

    // strafe normal
    private double currentSpeed = 0.0D;
    private double prevMotion = 0.0D;
    private boolean oddStage = false;
    private int state = 4;

    // aac
    private double aacSpeed = 0.2873D;
    private int aacCounter;
    private int aacState = 4;
    private int ticksPassed = 0;

    private boolean sneaking;

    private double maxVelocity = 0;
    private Timer velocityTimer = new Timer();

    private Timer setbackTimer = new Timer();

    private int lowHopStage;
    private double lowHopSpeed;
    private boolean even;

    private int onGroundStage;
    private double onGroundSpeed;
    private boolean forceGround;

    private Mode getMode() {
        /*if (PlayerUtils.isKeyDown(altBind.getValue().getKeyCode())) {
            return altMode.getValue();
        }*/
        return mode.getValue();
    }

    @Listener
    public void onPlayerUseItem(PlayerUseItemEvent event) {
        if (!sneaking && getMode() == Mode.STRAFESTRICT && allowEat.getValue()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (event.getPhase() == TickEvent.Phase.START && getMode() == Mode.ONGROUND) {
            double dX = mc.player.posX - mc.player.prevPosX;
            double dZ = mc.player.posZ - mc.player.prevPosZ;
            prevMotion = Math.sqrt(dX * dX + dZ * dZ);
        }
        /*if (ElytraFly.isEnabled() && ElytraFly.isHasElytra()) {
            Zenith.INSTANCE.timerManager.resetTimer(this);
            return;
        }*/
        int minY = MathHelper.floor(mc.player.getEntityBoundingBox().minY - 0.2D);
        //boolean inLiquid = Jesus.checkIfBlockInBB(BlockLiquid.class, minY) != null;
        //if (Jesus.isEnabled() && (mc.player.isInWater() || mc.player.isInLava() || inLiquid)) return;
        setFeatureMetadata(getMode().name().substring(0, 1).toUpperCase() + getMode().name().substring(1).toLowerCase());
        if(disableOnSneak.getValue() && mc.player.isSneaking()) return;
        if ((getMode() == Mode.STRAFEOLD || getMode() == Mode.STRAFE || getMode() == Mode.LOWHOP) && useTimer.getValue()) {
            Zenith.INSTANCE.timerManager.updateTimer(this, 10, 1.080f + (0.008f * timerFactor.getValue()));
        } else if (getMode() != Mode.STRAFESTRICT && getMode() != Mode.SMALLHOP) {
            Zenith.INSTANCE.timerManager.resetTimer(this);
        }
        switch (getMode()) {
            case STRAFEOLD: {
                /*if (ModuleManager.getModuleByName("LongJump").isEnabled() && LongJump.disableStrafe.getValue())
                    return;
                if (ModuleManager.getModuleByName("ElytraFly").isEnabled())
                    return;*/
                if (forceSprint.getValue()) {
                    if (!mc.player.isSprinting() && PlayerUtils.isPlayerMoving()) {
                        mc.player.setSprinting(true);
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    }
                }

                ncpPrevMotion = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
                break;
            }
            case SMALLHOP: {
                // Ily auto
                if (!PlayerUtils.isPlayerMoving() || mc.player.collidedHorizontally) {
                    Zenith.INSTANCE.timerManager.resetTimer(this);
                    return;
                }
                if (mc.player.onGround) {
                    Zenith.INSTANCE.timerManager.updateTimer(this, 10, 1.15f);
                    mc.player.jump();
                    boolean ice = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockPackedIce;
                    double[] dirSpeed = PlayerUtils.directionSpeed((getBaseMotionSpeed() * speed.getValue()) + (boost.getValue() ? (ice ? 0.3 : 0.06 * boostFactor.getValue()) : 0D));
                    mc.player.motionX = dirSpeed[0];
                    mc.player.motionZ = dirSpeed[1];
                } else {
                    mc.player.motionY = -1;
                    Zenith.INSTANCE.timerManager.resetTimer(this);
                }
                break;
            }
        }

        Item item = mc.player.getActiveItemStack().getItem();
        if (getMode() == Mode.STRAFESTRICT && allowEat.getValue() && sneaking && ((!mc.player.isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            sneaking = false;
        }
    }

    @Listener
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if(mc.player == null || mc.world == null) return;

        /*if (ElytraFly.isEnabled() && ElytraFly.isHasElytra()) {
            return;
        }*/

        switch (getMode()) {
            case TP: {
                for (double x = 0.0625; x < speed.getValue(); x += 0.262) {
                    double[] dir = PlayerUtils.directionSpeed(x);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + dir[0], mc.player.posY, mc.player.posZ + dir[1], mc.player.onGround));
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
            }
        }
    }

    @Listener//(priority = 1000)
    public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent event) {

        if (event.getStage() != EventStageable.EventStage.PRE) return;

        /*if (ElytraFly.isEnabled() && ElytraFly.isHasElytra()) {
            return;
        }*/

        if (!PlayerUtils.isPlayerMoving()) {
            currentSpeed = 0.0;
            if (getMode() != Mode.SMALLHOP) {
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                return;
            }
        }

        if (getMode() == Mode.STRAFE || getMode() == Mode.STRAFESTRICT || getMode() == Mode.LOWHOP) {
            double dX = mc.player.posX - mc.player.prevPosX;
            double dZ = mc.player.posZ - mc.player.prevPosZ;
            prevMotion = Math.sqrt(dX * dX + dZ * dZ);
        }
    }

    @Listener
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && forceGround) {
            forceGround = false;
            ((ICPacketPlayer) event.getPacket()).setOnGround(true);
        }
    }

    @Listener//(priority = 69)
    public void onPacketReceive(PacketReceiveEvent event) {
        /*if (ElytraFly.isEnabled() && ElytraFly.isHasElytra()) {
            return;
        }*/

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            Zenith.INSTANCE.timerManager.resetTimer(this);
            ncpPrevMotion = 0.0D;
            currentSpeed = 0.0D;
            horizontal = 0;
            state = 4;
            aacSpeed = 0.2873;
            aacState = 4;
            prevMotion = 0;
            aacCounter = 0;
            maxVelocity = 0;
            setbackTimer.reset();
            lowHopStage = 4;
            onGroundStage = 2;
            onGroundSpeed = 0;
        } else if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion velocity = (SPacketExplosion) event.getPacket();
            maxVelocity = Math.sqrt(velocity.getMotionX() * velocity.getMotionX() + velocity.getMotionZ() * velocity.getMotionZ());
            velocityTimer.reset();
        }
    }

    @Listener
    public void onMove(MoveEvent event) {
        if (mc.player == null || mc.world == null) return;
        if(disableOnSneak.getValue() && mc.player.isSneaking()) return;

        /*if (ElytraFly.isEnabled() && ElytraFly.isHasElytra()) {
            return;
        }*/

        int minY = MathHelper.floor(mc.player.getEntityBoundingBox().minY - 0.2D);
        //boolean inLiquid = Jesus.checkIfBlockInBB(BlockLiquid.class, minY) != null;
        //if (Jesus.isEnabled() && (mc.player.isInWater() || mc.player.isInLava() || inLiquid)) return;

        switch (getMode()) {
            // Normal Strafe
            case STRAFE: {
                if (state != 1 || (mc.player.moveForward == 0.0f || mc.player.moveStrafing == 0.0f)) {
                    if (state == 2 && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f)) {
                        double jumpSpeed = 0.0D;

                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            jumpSpeed += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                        }

                        mc.player.motionY = (hypixel.getValue() ? 0.3999999463558197D : 0.3999D) + jumpSpeed;
                        event.setY(mc.player.motionY);
                        currentSpeed *= oddStage ? 1.6835D : 1.395D;
                    } else if (state == 3) {
                        double adjustedMotion = 0.66D * (prevMotion - getBaseMotionSpeed());
                        currentSpeed = prevMotion - adjustedMotion;
                        oddStage = !oddStage;
                    } else {
                        List<AxisAlignedBB> collisionBoxes = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0));
                        if ((collisionBoxes.size() > 0 || mc.player.collidedVertically) && state > 0) {
                            state = mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f ? 0 : 1;
                        }
                        currentSpeed = prevMotion - prevMotion / 159.0;
                    }
                } else {
                    currentSpeed = 1.35D * getBaseMotionSpeed() - 0.01D;
                }

                currentSpeed = Math.max(currentSpeed, getBaseMotionSpeed());

                if (maxVelocity > 0 && boost.getValue() && !velocityTimer.hasPassed(75) && !mc.player.collidedHorizontally) {
                    currentSpeed = Math.max(currentSpeed, maxVelocity);
                } else if (strict.getValue()) {
                    currentSpeed = Math.min(currentSpeed, 0.433D);
                }

                double forward = mc.player.movementInput.moveForward;
                double strafe = mc.player.movementInput.moveStrafe;
                float yaw = mc.player.rotationYaw;

                if (forward == 0.0D && strafe == 0.0D) {
                    event.setX(0.0D);
                    event.setZ(0.0D);
                } else {
                    if (forward != 0.0D) {
                        if (strafe > 0.0D) {
                            yaw += (float)(forward > 0.0D ? -45 : 45);
                        } else if (strafe < 0.0D) {
                            yaw += (float)(forward > 0.0D ? 45 : -45);
                        }

                        strafe = 0.0D;

                        if (forward > 0.0D) {
                            forward = 1.0D;
                        } else if (forward < 0.0D) {
                            forward = -1.0D;
                        }
                    }

                    event.setX(forward * currentSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * currentSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
                    event.setZ(forward * currentSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * currentSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
                }


                if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) {
                    return;
                }

                state++;
                break;
            }
            // Legacy NCP Strafe - instead of directly modifing movement we do manual calculations
            case STRAFEOLD: {
                /*if (getMode() == Mode.STRAFEOLD && ModuleManager.getModuleByName("LongJump").isEnabled() && LongJump.disableStrafe.getValue())
                    return;
                if (getMode() == Mode.STRAFEOLD && ModuleManager.getModuleByName("ElytraFly").isEnabled())
                    return;*/
                if (!mc.player.isSprinting()) {
                    mc.player.setSprinting(true);
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }

                double adjustedSpeed = speed.getValue() * 0.99;

                double vertical;

                switch (strafeStage) {
                    case 0:
                        strafeStage++;
                        ncpPrevMotion = 0.0D;
                        break;
                    case 2:
                        vertical = hypixel.getValue() ? 0.3999999463558197D : 0.40123128D;
                        if ((mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
                            event.setY(mc.player.motionY = vertical);
                            horizontal *= 2.149D;
                        }
                        break;
                    case 3:
                        horizontal = ncpPrevMotion - 0.76D * (ncpPrevMotion - getBaseMotionSpeed());
                        break;
                    default:
                        horizontal = ncpPrevMotion - ncpPrevMotion / 159D;
                        if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0 || mc.player.collidedVertically) && strafeStage > 0) {
                            strafeStage = (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) ? 1 : 0;
                        }
                        break;
                }

                horizontal = Math.max(horizontal, getBaseMotionSpeed());

                if (maxVelocity > 0 && boost.getValue() && !velocityTimer.hasPassed(75) && !mc.player.collidedHorizontally) {
                    horizontal = Math.max(horizontal, maxVelocity);
                }

                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;

                if (forward == 0 && strafe == 0) {
                    event.setX(0D);
                    event.setZ(0D);
                } else if (forward != 0.0D && strafe != 0.0D) {
                    // Magical Numbers wtf :flushed:
                    forward *= Math.sin(0.7853981633974483D);
                    strafe *= Math.cos(0.7853981633974483D);
                }
                event.setX((forward * horizontal * -Math.sin(Math.toRadians(mc.player.rotationYaw)) + strafe * horizontal * Math.cos(Math.toRadians(mc.player.rotationYaw))) * adjustedSpeed);
                event.setZ((forward * horizontal * Math.cos(Math.toRadians(mc.player.rotationYaw)) - strafe * horizontal * -Math.sin(Math.toRadians(mc.player.rotationYaw))) * adjustedSpeed);

                strafeStage++;
                break;
            }
            // Strict NPC Strafe for NCP-Updated
            case STRAFESTRICT: {
                aacCounter++;
                aacCounter %= 5;

                if (aacCounter != 0) {
                    Zenith.INSTANCE.timerManager.resetTimer(this);
                } else if (PlayerUtils.isPlayerMoving()) {
                    Zenith.INSTANCE.timerManager.updateTimer(this, 10, 1.3F);
                    mc.player.motionX *= 1.0199999809265137D;
                    mc.player.motionZ *= 1.0199999809265137D;
                }

                if (mc.player.onGround && PlayerUtils.isPlayerMoving()) {
                    aacState = 2;
                }

                if (round(mc.player.posY - (int)mc.player.posY, 3) == round(0.138D, 3)) {
                    mc.player.motionY -= 0.08D;
                    event.setY(event.getY() - 0.09316090325960147D);
                    mc.player.posY -= 0.09316090325960147D;
                }

                if (aacState == 1 && (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F)) {
                    aacState = 2;
                    aacSpeed = 1.38D * getBaseMotionSpeed() - 0.01D;
                } else if (aacState == 2) {
                    aacState = 3;
                    mc.player.motionY = 0.399399995803833D;
                    event.setY(0.399399995803833D);
                    aacSpeed *= 2.149D;
                } else if (aacState == 3) {
                    aacState = 4;
                    double adjustedMotion = 0.66D * (prevMotion - getBaseMotionSpeed());
                    aacSpeed = prevMotion - adjustedMotion;
                } else {
                    if (mc.world.getCollisionBoxes(mc.player, mc.player
                            .getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0 || mc.player.collidedVertically)
                        aacState = 1;
                    aacSpeed = prevMotion - prevMotion / 159.0D;
                }

                aacSpeed = Math.max(aacSpeed, getBaseMotionSpeed());

                if (maxVelocity > 0 && boost.getValue() && !velocityTimer.hasPassed(75) && !mc.player.collidedHorizontally) {
                    aacSpeed = Math.max(aacSpeed, maxVelocity);
                } else {
                    aacSpeed = Math.min(aacSpeed, (ticksPassed > 25) ? 0.449D : 0.433D);
                }

                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;
                float yaw = mc.player.rotationYaw;

                ticksPassed++;

                if (ticksPassed > 50)
                    ticksPassed = 0;
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0.0D);
                    event.setZ(0.0D);
                } else if (forward != 0.0F) {
                    if (strafe >= 1.0F) {
                        yaw += ((forward > 0.0F) ? -45 : 45);
                        strafe = 0.0F;
                    } else if (strafe <= -1.0F) {
                        yaw += ((forward > 0.0F) ? 45 : -45);
                        strafe = 0.0F;
                    }
                    if (forward > 0.0F) {
                        forward = 1.0F;
                    } else if (forward < 0.0F) {
                        forward = -1.0F;
                    }
                }

                double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
                double sin = Math.sin(Math.toRadians((yaw + 90.0F)));

                event.setX(forward * aacSpeed * cos + strafe * aacSpeed * sin);
                event.setZ(forward * aacSpeed * sin - strafe * aacSpeed * cos);

                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0.0D);
                    event.setZ(0.0D);
                }

                break;
            }
            // LowHop Speed
            case LOWHOP: {
                if (!setbackTimer.hasPassed(100L)) return;

                double jumpSpeed = 0.0D;

                if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    jumpSpeed += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                }

                if (round(mc.player.posY - (double) (int) mc.player.posY, 3) == round(0.4, 3)) {
                    mc.player.motionY = 0.31 + jumpSpeed;
                    event.setY(mc.player.motionY);
                } else if (round(mc.player.posY - (double) (int) mc.player.posY, 3) == round(0.71, 3)) {
                    mc.player.motionY = 0.04 + jumpSpeed;
                    event.setY(mc.player.motionY);
                } else if (round(mc.player.posY - (double) (int) mc.player.posY, 3) == round(0.75, 3)) {
                    mc.player.motionY = -0.2 - jumpSpeed;
                    event.setY(mc.player.motionY);
                } else if (round(mc.player.posY - (double) (int) mc.player.posY, 3) == round(0.55, 3)) {
                    mc.player.motionY = -0.14 + jumpSpeed;
                    event.setY(mc.player.motionY);
                } else {
                    if (round(mc.player.posY - (double) (int) mc.player.posY, 3) == round(0.41, 3)) {
                        mc.player.motionY = -0.2 + jumpSpeed;
                        event.setY(mc.player.motionY);
                    }
                }

                if (lowHopStage == 1 && (mc.player.moveForward != 0F || mc.player.moveStrafing != 0F)) {
                    lowHopSpeed = 1.35 * getBaseMotionSpeed() - 0.01;
                } else if (lowHopStage == 2 && (mc.player.moveForward != 0F || mc.player.moveStrafing != 0F)) {
                    mc.player.motionY = (checkHeadspace() ? 0.2 : 0.3999) + jumpSpeed;
                    event.setY(mc.player.motionY);
                    lowHopSpeed = lowHopSpeed * (even ? 1.5685 : 1.3445);
                } else if (lowHopStage == 3) {
                    double dV = 0.66 * (prevMotion - getBaseMotionSpeed());
                    lowHopSpeed = prevMotion - dV;
                    even = !even;
                } else {
                    if (mc.player.onGround && lowHopStage > 0) {
                        lowHopStage = mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f ? 1 : 0;
                    }
                    lowHopSpeed = prevMotion - prevMotion / 159.0D;
                }

                lowHopSpeed = Math.max(lowHopSpeed, getBaseMotionSpeed());

                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;

                if (forward == 0 && strafe == 0) {
                    event.setX(0D);
                    event.setZ(0D);
                } else if (forward != 0.0D && strafe != 0.0D) {
                    forward *= Math.sin(0.7853981633974483D);
                    strafe *= Math.cos(0.7853981633974483D);
                }
                event.setX(forward * lowHopSpeed * -Math.sin(Math.toRadians(mc.player.rotationYaw)) + strafe * lowHopSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw)));
                event.setZ(forward * lowHopSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw)) - strafe * lowHopSpeed * -Math.sin(Math.toRadians(mc.player.rotationYaw)));

                if (mc.player.moveForward == 0F && mc.player.moveStrafing == 0F) return;
                lowHopStage++;
                break;
            }
            case ONGROUND: {
                if (!mc.player.onGround) {
                    if (onGroundStage != 3) return;
                }
                if (!((mc.player.collidedHorizontally || mc.player.moveForward == 0) && mc.player.moveStrafing == 0)) {
                    if (onGroundStage == 2) {
                        mc.player.motionY = -0.5;
                        event.setY(checkHeadspace() ? 0.2 : 0.4);
                        onGroundSpeed *= 2.149;
                        onGroundStage = 3;

                        if (bypass.getValue()) {
                            forceGround = true;
                        }
                    } else if (onGroundStage == 3) {
                        double adjustedSpeed = 0.66 * (prevMotion - getBaseMotionSpeed());
                        onGroundSpeed = prevMotion - adjustedSpeed;
                        onGroundStage = 2;
                    } else {
                        if (checkHeadspace() || mc.player.collidedVertically) {
                            onGroundStage = 1;
                        }
                    }
                }

                onGroundSpeed = Math.min(Math.max(onGroundSpeed, getBaseMotionSpeed()), speed.getValue());

                float forward = mc.player.movementInput.moveForward;
                float strafe = mc.player.movementInput.moveStrafe;

                if (forward == 0 && strafe == 0) {
                    event.setX(0D);
                    event.setZ(0D);
                } else if (forward != 0.0D && strafe != 0.0D) {
                    forward *= Math.sin(0.7853981633974483D);
                    strafe *= Math.cos(0.7853981633974483D);
                }
                event.setX(forward * onGroundSpeed * -Math.sin(Math.toRadians(mc.player.rotationYaw)) + strafe * onGroundSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw)));
                event.setZ(forward * onGroundSpeed * Math.cos(Math.toRadians(mc.player.rotationYaw)) - strafe * onGroundSpeed * -Math.sin(Math.toRadians(mc.player.rotationYaw)));

                break;
            }
        }
    }

    private boolean checkHeadspace() {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0D, 0.21D, 0D)).size() > 0;
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        maxVelocity = 0;
        hopStage = 1;
        lowHopStage = 4;
        onGroundStage = 2;
        /*if (getMode() == Mode.STRAFEOLD && ModuleManager.getModuleByName("LongJump").isEnabled() && LongJump.disableStrafe.getValue())
            return;*/
        switch (getMode()) {
            case STRAFEOLD: {
                if (!mc.player.isSprinting() && PlayerUtils.isPlayerMoving()) {
                    mc.player.setSprinting(true);
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
            }
            case STRAFE: {
                state = 4;
                currentSpeed = getBaseMotionSpeed();
                prevMotion = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) return;
        if (getMode() == Mode.SMALLHOP) {
            mc.player.setVelocity(0, 0, 0);
        }
        Zenith.INSTANCE.timerManager.resetTimer(this);
    }

    private double getBaseMotionSpeed() {
        double baseSpeed = (getMode() == Mode.STRAFE || getMode() == Mode.STRAFESTRICT || getMode() == Mode.SMALLHOP || getMode() == Mode.ONGROUND || getMode() == Mode.LOWHOP) ? 0.2873D : 0.272D;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * ((double) amplifier + 1);
        }
        return baseSpeed;
    }

    private double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}