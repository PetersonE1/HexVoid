package org.agent.hexvoid.blocks.portal_mapper

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.agent.hexvoid.blocks.debug_portal.HorizontalBlock

@Suppress("OVERRIDE_DEPRECATION")
class PortalMapperFull(properties: Properties) : HorizontalBlock(properties) {
    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        return InteractionResult.SUCCESS
    }


}