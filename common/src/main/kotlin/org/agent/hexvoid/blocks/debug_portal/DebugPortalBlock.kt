package org.agent.hexvoid.blocks.debug_portal

import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import org.agent.hexvoid.casting.iotas.RealityScentIota
import org.agent.hexvoid.functionality.InterstitiaTeleport

@Suppress("OVERRIDE_DEPRECATION")
class DebugPortalBlock(properties: Properties) : HorizontalBlock(properties) {
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
            is RealityScentIota -> iota.globalPos
            is NullIota -> null
            else -> return InteractionResult.SUCCESS
        }

        return if (InterstitiaTeleport.teleportPlayer(player as ServerPlayer, globalPos)) InteractionResult.SUCCESS else InteractionResult.FAIL
    }
}