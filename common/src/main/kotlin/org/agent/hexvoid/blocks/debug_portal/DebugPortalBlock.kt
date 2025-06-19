package org.agent.hexvoid.blocks.debug_portal

import net.minecraft.client.renderer.EffectInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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
            return InteractionResult.SUCCESS

        val server = player.server!!

        val origin = player.level() as ServerLevel
        val interstitia = server.allLevels.find { level -> level.dimension().location() == HexvoidDimensions.INTERSTITIA_LEVEL_KEY.location() }

        if (interstitia == null)
            return InteractionResult.FAIL

        if (interstitia == origin) {
            val homeDim = server.getLevel((player as ServerPlayer).respawnDimension)
            var antiSoftlock = false
            if (homeDim == null || homeDim == interstitia) {
                homeDim == server.overworld()
                antiSoftlock = true
            }
            val compressionFactor = 1 / homeDim!!.dimensionType().coordinateScale
            var spawnpoint = player.respawnPosition
            spawnpoint = if (spawnpoint == null || antiSoftlock) {
                homeDim.sharedSpawnPos!!
            } else {
                BlockPos((spawnpoint.x * compressionFactor).toInt(), spawnpoint.y, (spawnpoint.z * compressionFactor).toInt())
            }
            player.teleportTo(homeDim, spawnpoint.x + 0.5, spawnpoint.y.toDouble(), spawnpoint.z + 0.5, player.yRot, player.xRot)
            player.onUpdateAbilities()
        } else {
            var altitude = 321.0
            while (interstitia.getBlockState(BlockPos(pos.x, altitude.toInt(), pos.z)).isAir) {
                altitude--
                if (altitude < -64) {
                    altitude = 321.0
                    break
                }
            }
            val compressionFactor = origin.dimensionType().coordinateScale
            var destPos = Vec3(floor(pos.x * compressionFactor) + 0.5, altitude + 1, floor(pos.z * compressionFactor) + 0.5)
            val border = interstitia.worldBorder

            destPos = border.clampToBounds(destPos.x, destPos.y, destPos.z).center

            (player as ServerPlayer).teleportTo(interstitia, destPos.x, destPos.y, destPos.z, player.yRot, player.xRot)
            player.onUpdateAbilities()
            if (altitude >= 321) {
                player.addEffect(MobEffectInstance(MobEffects.SLOW_FALLING, 1200))
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun hasAnalogOutputSignal(state: BlockState) = false

    companion object {
        val FACING = BlockStateProperties.HORIZONTAL_FACING
    }
}