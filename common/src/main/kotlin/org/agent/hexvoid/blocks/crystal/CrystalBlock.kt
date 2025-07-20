package org.agent.hexvoid.blocks.crystal

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

@Suppress("OVERRIDE_DEPRECATION")
class CrystalBlock(properties: Properties) : Block(properties) {
    init {
        registerDefaultState(
            getStateDefinition().any()
                .setValue(SHEEN, true)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(SHEEN)
    }

    override fun getRenderShape(state: BlockState) = RenderShape.MODEL

    companion object {
        val SHEEN : BooleanProperty = BooleanProperty.create("sheen")
    }
}