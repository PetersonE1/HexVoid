package org.agent.hexvoid.blocks.fluids.liquid_quartz

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.entity.TickingBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidFluids
import org.agent.hexvoid.registry.HexvoidItems

open class LiquidQuartz : FlowingFluid() {
    override fun isSame(fluid: Fluid): Boolean {
        return fluid == source || fluid == flowing
    }

    override fun getFlowing(): Fluid {
        return HexvoidFluids.LIQUID_QUARTZ_FLOWING.value
    }

    override fun getSource(): Fluid {
        return HexvoidFluids.LIQUID_QUARTZ.value
    }

    override fun getFlowing(level: Int, falling: Boolean): FluidState {
        return (this.flowing.defaultFluidState().setValue(LEVEL, level)).setValue(FALLING, falling)
    }

    override fun canConvertToSource(level: Level): Boolean {
        return false
    }

    override fun beforeDestroyingBlock(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) level.getBlockEntity(pos) else null
        Block.dropResources(state, level, pos, blockEntity)
    }

    override fun getSlopeFindDistance(level: LevelReader): Int {
        return 3
    }

    override fun getDropOff(level: LevelReader): Int {
        return 1
    }

    override fun getBucket(): Item {
        return HexvoidItems.LIQUID_QUARTZ_BUCKET.value
    }

    override fun canBeReplacedWith(
        state: FluidState,
        level: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return false
    }

    override fun getTickDelay(level: LevelReader): Int {
        return 5
    }

    override fun getExplosionResistance(): Float {
        return 100.0f
    }

    override fun createLegacyBlock(state: FluidState): BlockState {
        return LiquidQuartzBlock.INSTANCE.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state))
    }

    override fun isSource(state: FluidState): Boolean {
        return state.isSource
    }

    override fun getAmount(state: FluidState): Int {
        return 8
    }

    override fun `arch$holder`(): Holder<Fluid> {
        return super.`arch$holder`()
    }

    override fun `arch$registryName`(): ResourceLocation? {
        return super.`arch$registryName`()
    }

    companion object {
        val ID = Hexvoid.id("liquid_quartz")
        val FLOWING_ID = Hexvoid.id("flowing_liquid_quartz")
        val FLOWING_FLUID = LiquidQuartz.Flowing()
        val STILL_FLUID = LiquidQuartz.Still()
    }

    class Flowing : LiquidQuartz() {
        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun isSource(state: FluidState): Boolean {
            return false
        }

        override fun getAmount(state: FluidState): Int {
            return state.getValue(LEVEL)
        }
    }

    class Still : LiquidQuartz() {
        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun isSource(state: FluidState): Boolean {
            return true
        }

        override fun getAmount(state: FluidState): Int {
            return 8
        }
    }
}