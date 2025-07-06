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

    private var _portalActive: Boolean = false
    private var _targetPos: GlobalPos? = null

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        deactivatePortal()
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        super.onPlace(state, level, pos, oldState, movedByPiston)
        _portalActive = false
    }

    fun activatePortal(duration: Int, level: ServerLevel, pos: BlockPos, target: GlobalPos?) {
        if (!_portalActive) {
            _portalActive = true
            _targetPos = target
            level.scheduleTick(pos, this, duration)
        }
    }

    fun deactivatePortal() {
        _portalActive = false
    }

    fun teleport(player: ServerPlayer) {
        if (_portalActive) {
            InterstitiaTeleport.teleport(player, _targetPos)
        }
    }

    companion object {
        val ID = Hexvoid.id("liquid_quartz")
        val SETTINGS: Properties = Properties.copy(Blocks.WATER).noOcclusion()
        val INSTANCE = LiquidQuartzBlock(LiquidQuartz.STILL_FLUID, SETTINGS)
    }
}