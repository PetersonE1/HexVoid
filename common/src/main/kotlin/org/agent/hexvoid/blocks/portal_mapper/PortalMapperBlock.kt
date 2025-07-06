package org.agent.hexvoid.blocks.portal_mapper

import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import org.agent.hexvoid.blocks.debug_portal.HorizontalEntityBlock
import org.agent.hexvoid.blocks.fluids.liquid_quartz.LiquidQuartzBlock
import org.agent.hexvoid.casting.iotas.RealityScentIota
import org.agent.hexvoid.registry.HexvoidBlocks

@Suppress("OVERRIDE_DEPRECATION")
class PortalMapperBlock(properties: Properties) : HorizontalEntityBlock(properties) {
    init {
        registerDefaultState(
            getStateDefinition().any()
                .setValue(ITEM_STATE, PortalMapperItemState.EMPTY)
                .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder
            .add(ITEM_STATE)
            .add(FACING)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = PortalMapperBlockEntity(pos, state)

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        if (player.isShiftKeyDown || hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS
        }

        val blockEntity = getBlockEntity(level, pos) ?: return InteractionResult.PASS
        val heldItem = player.getItemInHand(hand)
        val storedItem = blockEntity.iotaStack

        fun swapItem(): InteractionResult {
            if (!level.isClientSide) {
                player.setItemInHand(hand, storedItem)
                blockEntity.iotaStack = heldItem
                blockEntity.sync()
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }

        return when {
            isValidItem(heldItem) -> swapItem()
            !heldItem.isEmpty -> InteractionResult.PASS
            !storedItem.isEmpty -> swapItem()
            else -> InteractionResult.PASS
        }
    }

    override fun playerWillDestroy(level: Level, pos: BlockPos, state: BlockState, player: Player) {
        getBlockEntity(level, pos)?.let { blockEntity ->
            if (!level.isClientSide && !blockEntity.isEmpty && player.isCreative) {
                val stack = ItemStack(this)
                blockEntity.saveToItem(stack)
                ItemEntity(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, stack).run {
                    setDefaultPickUpDelay()
                    level.addFreshEntity(this)
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player)
    }

    override fun getCloneItemStack(level: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
        val stack = super.getCloneItemStack(level, pos, state)
        getBlockEntity(level, pos)?.saveToItem(stack)
        return stack
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): MutableList<ItemStack> {
        var lootTableDrops = super.getDrops(state, params)

        val blockEntity = params.getBlockEntity<PortalMapperBlockEntity>()
        if (blockEntity == null || blockEntity.isEmpty) {
            return lootTableDrops
        }

        if (lootTableDrops.isEmpty()) {
            return blockEntity.stacks
        }

        val stack = ItemStack(this)
        blockEntity.saveToItem(stack)
        return mutableListOf(stack)
    }

    fun activatePortal(duration: Int, level: ServerLevel, pos: BlockPos) {
        val blockEntity = getBlockEntity(level, pos) ?: return
        val iota = blockEntity.readIota(level)
        val globalPos = when (iota) {
            is RealityScentIota -> iota.globalPos
            is NullIota -> null
            else -> return
        }

        val block = level.getBlockState(pos.below()).block as LiquidQuartzBlock
        block.activatePortal(duration, level, pos.below(), globalPos)
    }

    companion object {
        val ITEM_STATE: EnumProperty<PortalMapperItemState> = EnumProperty.create("item_state", PortalMapperItemState::class.java)

        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

        fun getBlockEntity(level: BlockGetter, pos: BlockPos) = level.getBlockEntity(pos) as? PortalMapperBlockEntity

        private fun isValidItem(stack: ItemStack) =
            IXplatAbstractions.INSTANCE.findDataHolder(stack) != null
                    && !stack.`is`(HexvoidBlocks.PORTAL_MAPPER_FULL.item) // TODO: use a tag instead?
    }
}

inline fun <reified T : BlockEntity> LootParams.Builder.getBlockEntity(): T? {
    return getOptionalParameter(LootContextParams.BLOCK_ENTITY) as? T
}