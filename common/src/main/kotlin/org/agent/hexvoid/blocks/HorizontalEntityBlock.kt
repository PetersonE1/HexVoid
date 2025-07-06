package org.agent.hexvoid.blocks.debug_portal

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlockEntity

@Suppress("OVERRIDE_DEPRECATION")
abstract class HorizontalEntityBlock(properties: Properties) : BaseEntityBlock(properties) {
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
    abstract override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity?

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
    }
}