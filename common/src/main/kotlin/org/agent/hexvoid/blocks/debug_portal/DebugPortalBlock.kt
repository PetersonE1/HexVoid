package org.agent.hexvoid.blocks.debug_portal

import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.client.renderer.EffectInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.GlobalPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.agent.hexvoid.casting.iotas.RealityFlavorIota
import org.agent.hexvoid.functionality.InterstitiaTeleport
import org.agent.hexvoid.world.dimension.HexvoidDimensions
import org.apache.logging.log4j.core.jmx.Server
import kotlin.math.floor

@Suppress("OVERRIDE_DEPRECATION")
class DebugPortalBlock(properties: Properties) : Block(properties) {
    init {
        registerDefaultState(
            getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext) =
        defaultBlockState().setValue(FACING, ctx.horizontalDirection.opposite)

    override fun mirror(state: BlockState, mirror: Mirror) =
        state.rotate(mirror.getRotation(state.getValue(FACING)))

    override fun rotate(state: BlockState, rotation: Rotation) =
        state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

    override fun getRenderShape(state: BlockState) = RenderShape.MODEL

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        if (level.isClientSide)
            return InteractionResult.PASS

        val dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(player.getItemInHand(hand))
        if (dataHolder == null)
            return InteractionResult.PASS

        val iota = dataHolder.readIota(level as ServerLevel)
        val globalPos = when (iota) {
            is RealityFlavorIota -> iota.globalPos
            is NullIota -> null
            else -> return InteractionResult.SUCCESS
        }

        return if (InterstitiaTeleport.teleport(player as ServerPlayer, globalPos)) InteractionResult.SUCCESS else InteractionResult.FAIL
    }

    override fun hasAnalogOutputSignal(state: BlockState) = false

    companion object {
        val FACING = BlockStateProperties.HORIZONTAL_FACING
    }
}