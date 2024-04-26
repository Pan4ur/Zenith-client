package me.gopro336.zenith.feature.toggleable.misc

import me.gopro336.zenith.event.network.PacketReceiveEvent
import me.gopro336.zenith.event.player.UpdateWalkingPlayerEvent
import me.gopro336.zenith.feature.Feature
import me.gopro336.zenith.property.NumberProperty
import me.gopro336.zenith.property.Property
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks.BEDROCK
import net.minecraft.inventory.ClickType
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.SPacketChat
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.common.ForgeHooks
import org.lwjgl.opengl.Display
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.regex.Pattern
import kotlin.random.Random

/**
 * @author Gopro336
 */
object AntiAfk: Feature() {
	private val whisperPattern: Pattern = Pattern.compile("/^([A-z0-9_])+ whispers.*/gm") // https://regexr.com/532bp
	private var lastRan = Instant.EPOCH
	private var lastKey = Instant.EPOCH

	var autoReply =
		Property(this, "AutoReply", "", true)
	var delay = NumberProperty(this, "Delay", "", 1, 1, 10)

	@Listener
	fun onUpdateWalkingPlayer(event: UpdateWalkingPlayerEvent?) {
		if (Duration.between(lastKey, Instant.now()).seconds >= 6) {
			lastKey = Instant.now()
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.keyCode, Random.nextBoolean())
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, Random.nextBoolean())
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.keyCode, Random.nextBoolean())
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.keyCode, Random.nextBoolean())
		}
		
		if (Duration.between(lastRan, Instant.now()).seconds >= delay.value) {
			lastRan = Instant.now()
			when (Random.nextInt(10)) {
				0  -> {
					mc.player.rotationYaw = if (Random.nextBoolean()) Random.nextInt(90).toFloat() else -Random.nextInt(
						90
					).toFloat()
					mc.player.rotationPitch = if (Random.nextBoolean()) Random.nextInt(90)
						.toFloat() else -Random.nextInt(90).toFloat()
					//TODO add rotations
					/*RotationManager.sendRotation(
						mc.player.rotationYaw,
						mc.player.rotationPitch,
						mc.player.onGround
					)*/
				}
				1  -> {
					mc.player.connection.sendPacket(CPacketAnimation(if (Random.nextBoolean()) EnumHand.MAIN_HAND else EnumHand.OFF_HAND))
				}
				2  -> run {
					if (mc.objectMouseOver != null) {
						when (mc.objectMouseOver.typeOfHit) {
							RayTraceResult.Type.ENTITY -> {
								mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit)
							}
							RayTraceResult.Type.BLOCK  -> {
								val blockpos = mc.objectMouseOver.blockPos
								if (!mc.world.isAirBlock(blockpos)) {
									mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit)
									return@run
								}
								ForgeHooks.onEmptyLeftClick(mc.player)
								mc.player.swingArm(EnumHand.MAIN_HAND)
							}
							RayTraceResult.Type.MISS   -> {
								ForgeHooks.onEmptyLeftClick(mc.player)
								mc.player.swingArm(EnumHand.MAIN_HAND)
							}
							null                       -> {
							}
						}
					}
				}
				3  -> {
					mc.player.isSneaking = Random.nextBoolean()
				}
				4  -> {
					mc.player.connection.sendPacket(
						CPacketTabComplete(
							"/" + UUID.randomUUID().toString().replace('-', 'v'),
							mc.player.position,
							false
						)
					)
				}
				5  -> {
					mc.player.jump()
				}
				6  -> {
					mc.player.sendChatMessage("/" + UUID.randomUUID().toString().replace('-', 'v'))
				}
				7  -> {
					mc.player.connection.sendPacket(
						CPacketClickWindow(
							1,
							1,
							1,
							ClickType.CLONE,
							ItemStack(BEDROCK),
							1.toShort()
						)
					)
				}
				8  -> {
					mc.player.connection.sendPacket(
						CPacketPlayerDigging(
							CPacketPlayerDigging.Action.SWAP_HELD_ITEMS,
							mc.player.position,
							EnumFacing.DOWN
						)
					)
				}
				9  -> {
					mc.player.connection.sendPacket(
						CPacketPlayerDigging(
							CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
							mc.player.position,
							EnumFacing.DOWN
						)
					)
				}
				10 -> {
					val slot: Int = Random.nextInt(9)
					mc.player.inventory.currentItem = slot
					mc.player.connection.sendPacket(CPacketHeldItemChange(slot))
				}
			}
		}
	}

	@Listener
	fun onPacket(event: PacketReceiveEvent?) {
		if (event != null) {
			if (autoReply.value && event.packet is SPacketChat && Display.isActive()) {
				val packet = event.packet as SPacketChat
				val message = packet.chatComponent.unformattedText

				if (whisperPattern.matcher(message).matches()) {
					mc.player.connection.sendPacket(CPacketChatMessage("/r I am currently AFK using Zenith"))
				}
			}
		}
	}
}
