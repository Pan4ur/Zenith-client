package me.gopro336.zenith.api.util.kotlin

import me.gopro336.zenith.api.util.Wrapper
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos

object SurroundUtils {
    private val mc = Wrapper.mc
    val Entity.flooredPosition get() = BlockPos(floor.floorToInt(posX), floor.floorToInt(posY), floor.floorToInt(posZ))

    val surroundOffset = arrayOf(
        BlockPos(0, -1, 0), // down
        BlockPos(0, 0, -1), // north
        BlockPos(1, 0, 0),  // east
        BlockPos(0, 0, 1),  // south
        BlockPos(-1, 0, 0)  // west
    )

    fun checkHole(entity: Entity) =
        checkHole(entity.flooredPosition)

    fun checkHole(pos: BlockPos): HoleType {
        // Must be a 1 * 3 * 1 empty space
        if (!mc.world.isAirBlock(pos) || !mc.world.isAirBlock(pos.up()) || !mc.world.isAirBlock(pos.up().up())) return HoleType.NONE

        var type = HoleType.BEDROCK

        for (offset in surroundOffset) {
            val block = mc.world.getBlockState(pos.add(offset)).block

            if (!checkBlock(block)) {
                type = HoleType.NONE
                break
            }

            if (block != Blocks.BEDROCK) type = HoleType.OBBY
        }

        return type
    }

    private fun checkBlock(block: Block): Boolean {
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL
    }

    enum class HoleType {
        NONE, OBBY, BEDROCK
    }
}