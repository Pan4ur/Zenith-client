package me.gopro336.zenith.feature.toggleable.Combat

import me.gopro336.zenith.feature.AnnotationHelper
import me.gopro336.zenith.feature.Category
import me.gopro336.zenith.feature.Feature

/**
 * @author Gopro336
 * why is this fucked :(
 */
@AnnotationHelper(name = "HS", description = "", category = Category.MISC)
object HoleSnapKT: Feature() {/*

    var range = NumberProperty(this, "Speed", "", 0.25f, 0.5f, 2.5f)
    var airStrafe =
        Property(this, "AirStrafe", "", true)
    var disableStrafe =
        Property(this, "DisableStrafe", "", true)

    var testBP =
        Property(this, "TestBlockpos", "", true)

    val Entity.flooredPosition get() = BlockPos(floorToInt(posX), floorToInt(posY), floorToInt(posZ))

    private var hoePos: BlockPos? = null
    private var holePos: BlockPos? = null
    private var stuckTicks = 0

    override fun disable() {
        super.disable()
        holePos = null
        stuckTicks = 0
        sendMessage("HoleSnapDisabled!", true)
    }

    @Listener
    fun onPacket(packet: PacketReceiveEvent) {
        //if (packet is SPacketPlayerPosLook) disable()
    }

    //forge Event
    @Listener
    fun onInputUpdateEvent(event: InputUpdateEvent) {
        if (event.movementInput is MovementInputFromOptions && holePos != null) {
            event.movementInput.resetMove()
        }
    }

    @Listener
    fun onMove(event: MoveEvent) {
        if (!mc.player.isEntityAlive) return

        val currentSpeed = mc.player.speed

        /*if (shouldDisable(currentSpeed)) {
            disable()
            return
        }*/





        getHole()?.let {
            //if (disableStrafe) Strafe.disable()
            if (testBP.value) sendMessage("X "+it.getX()+" Y "+it.getY()+" Z "+it.getZ()+" ", true)

            if ((airStrafe.value || mc.player.onGround) && !mc.player.isCentered(it)) {
                val playerPos = mc.player.positionVector
                val targetPos = Vec3d(it.x + 0.5, mc.player.posY, it.z + 0.5)

                val yawRad = toRadian(getRotationTo(playerPos, targetPos).x)
                val dist = playerPos.distanceTo(targetPos)
                val speed = if (mc.player.onGround) min(0.2805, dist / 2.0) else currentSpeed + 0.02

                mc.player.motionX = -sin(yawRad) * speed
                mc.player.motionZ = cos(yawRad) * speed

                if (mc.player.collidedHorizontally) stuckTicks++
                else stuckTicks = 0
            }
        } ?: sendMessage("it was null")
    }

    fun Entity.isCentered(pos: BlockPos) =
        this.posX in pos.x + 0.31..pos.x + 0.69
                && this.posZ in pos.z + 0.31..pos.z + 0.69

    //fun onRender3D() {
    @Listener
    fun onRender(event: RenderWorldLastEvent) {
        holePos?.let {
            if (mc.player.flooredPosition == it) return

            val posFrom = LambdaTessellator.getInterpolatedPos(mc.player, LambdaTessellator.pTicks())
            val posTo = it.toVec3d(0.5, 0.0, 0.5)
            val buffer = LambdaTessellator.buffer

            GL11.glLineWidth(3.0f)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            LambdaTessellator.begin(GL11.GL_LINES)

            buffer.pos(posFrom.x, posFrom.y, posFrom.z).color(32, 255, 32, 255).endVertex()
            buffer.pos(posTo.x, posTo.y, posTo.z).color(32, 255, 32, 255).endVertex()

            LambdaTessellator.render()
            GL11.glLineWidth(1.0f)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
        }
    }

    private fun shouldDisable(currentSpeed: Double) =
        holePos?.let { mc.player.posY < it.y } ?: false
                || stuckTicks > 5 && currentSpeed < 0.1
                || currentSpeed < 0.01 && checkHole(mc.player) != SurroundUtils.HoleType.NONE

    private fun getHole() =
        if (mc.player.ticksExisted % 10 == 0 && mc.player.flooredPosition != holePos) getNearestHole()
        else holePos ?: getNearestHole()

    private fun getNearestHole(): BlockPos? {
        var closestHole = Pair(69.69, BlockPos.ORIGIN)
        val playerPos = mc.player.flooredPosition
        val ceilRange = ceilToInt(range.value)
        val posList = VectorUtils.getBlockPositionsInArea(playerPos.add(ceilRange, -1, ceilRange), playerPos.add(-ceilRange, -1, -ceilRange))

        for (posXZ in posList) {
            val dist = mc.player.distanceTo(posXZ)
            if (dist > range.value || dist > closestHole.first) continue

            for (posY in 0..5) {
                val pos = posXZ.add(0, -posY, 0)
                if (!mc.world.isAirBlock(pos.up())) break
                if (checkHole(pos) == SurroundUtils.HoleType.NONE) continue
                closestHole = dist to pos
            }
        }

        return if (closestHole.second != BlockPos.ORIGIN) closestHole.second.also { holePos = it }
        else null
    }

    fun getRotationTo(posTo: Vec3d): Vec2f {
        return getRotationTo(mc.player.getPositionEyes(1f), posTo)
    }

    /**
     * Get rotation from a position vector to another position vector
     *
     * @param posFrom Calculate rotation from this position vector
     * @param posTo Calculate rotation to this position vector
     */
    fun getRotationTo(posFrom: Vec3d, posTo: Vec3d): Vec2f {
        return getRotationFromVec(posTo.subtract(posFrom))
    }

    private fun normalizeAngle(angleIn: Double): Double {
        var angle = angleIn
        angle %= 360.0
        if (angle >= 180.0) {
            angle -= 360.0
        }
        if (angle < -180.0) {
            angle += 360.0
        }
        return angle
    }

    fun normalizeAngle(angleIn: Float): Float {
        var angle = angleIn
        angle %= 360f
        if (angle >= 180f) {
            angle -= 360f
        }
        if (angle < -180f) {
            angle += 360f
        }
        return angle
    }

    fun getRotationFromVec(vec: Vec3d): Vec2f {
        val xz = hypot(vec.x, vec.z)
        val yaw = normalizeAngle(Math.toDegrees(atan2(vec.z, vec.x)) - 90.0)
        val pitch = normalizeAngle(Math.toDegrees(-atan2(vec.y, xz)))
        return Vec2f(yaw, pitch)
    }*/
}