package org.agent.hexvoid.blocks.fluids.liquid_quartz

import at.petrak.hexcasting.api.mod.HexTags
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.pathfinder.PathComputationType
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.functionality.InterstitiaTeleport

class LiquidQuartzBlock(fluid: FlowingFluid, properties: Properties) : LiquidBlock(fluid, properties) {
    override fun isPathfindable(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        type: PathComputationType
    ): Boolean {
        return true
    }

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return true
    }

    companion object {
        val ID = Hexvoid.id("liquid_quartz")
        val SETTINGS: Properties = Properties.copy(Blocks.WATER).noOcclusion()
        val INSTANCE = LiquidQuartzBlock(LiquidQuartz.STILL_FLUID, SETTINGS)
    }
}