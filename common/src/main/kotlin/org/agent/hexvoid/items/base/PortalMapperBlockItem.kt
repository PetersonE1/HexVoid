package org.agent.hexvoid.items.base

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.getCompound
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.getString
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperItemState
import org.agent.hexvoid.utils.asItemPredicate
import org.agent.hexvoid.utils.styledHoverName

class PortalMapperBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties), IotaHolderItem, ItemPredicateProvider
{
    override fun readIotaTag(stack: ItemStack): CompoundTag? {
        val (iotaStack, iotaHolder) = stack.getIotaStack()
        return iotaHolder?.readIotaTag(iotaStack)
    }

    override fun writeable(stack: ItemStack): Boolean {
        val (_, iotaHolder) = stack.getIotaStack()
        return iotaHolder?.writeable(stack) ?: false
    }

    override fun canWrite(stack: ItemStack, iota: Iota?): Boolean {
        val (iotaStack, iotaHolder) = stack.getIotaStack()
        return iotaHolder?.canWrite(iotaStack, iota) ?: false
    }

    override fun writeDatum(stack: ItemStack, iota: Iota?) {
        val (iotaStack, iotaHolder) = stack.getIotaStack()
        if (iotaHolder != null) {
            iotaHolder.writeDatum(iotaStack, iota)
            stack.putIotaStack(iotaStack)
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        val (iotaStack, iotaHolder) = stack.getIotaStack()
        if (iotaHolder != null) {
            tooltipComponents += "hexvoid.tooltip.portal_mapper.item".asTranslatedComponent(iotaStack.styledHoverName)
            IotaHolderItem.appendHoverText(iotaHolder, iotaStack, tooltipComponents, isAdvanced)
        }
    }

    override fun getModelPredicates() = listOf(
        ModelPredicateEntry(ITEM_STATE) { stack, _, _, _ ->
            if (stack.hasIotaStack) {
                val iotaTag = stack.getIotaStack().second?.readIotaTag(stack)
                when {
                    iotaTag == null -> PortalMapperItemState.EMPTY.asItemPredicate
                    iotaTag.getString("hexcasting:type") == "hexcasting:null" -> PortalMapperItemState.EMPTY.asItemPredicate
                    iotaTag.getString("hexcasting:type") == "hexvoid:reality_scent" -> PortalMapperItemState.SCENT.asItemPredicate
                    else -> PortalMapperItemState.INVALID.asItemPredicate
                }
            } else {
                PortalMapperItemState.EMPTY.asItemPredicate
            }
        }
    )

    companion object {
        val ITEM_STATE = Hexvoid.id("item_state")

        val ItemStack.hasIotaStack get() = getCompound(BLOCK_ENTITY_TAG)
            ?.getList("Items", Tag.TAG_COMPOUND)
            ?.let { it.size > 0 }
            ?: false

        fun ItemStack.getIotaStack(): Pair<ItemStack, IotaHolderItem?> {
            val blockEntityTag = getCompound(BLOCK_ENTITY_TAG) ?: CompoundTag()

            val containerStacks = NonNullList.withSize(1, ItemStack.EMPTY)
            ContainerHelper.loadAllItems(blockEntityTag, containerStacks)

            val iotaStack = containerStacks.first()
            return Pair(iotaStack, iotaStack.item as? IotaHolderItem)
        }

        fun ItemStack.putIotaStack(iotaStack: ItemStack) {
            val blockEntityTag = getCompound(BLOCK_ENTITY_TAG) ?: CompoundTag()

            val containerStacks = NonNullList.of(ItemStack.EMPTY, iotaStack)
            ContainerHelper.saveAllItems(blockEntityTag, containerStacks)

            putCompound(BLOCK_ENTITY_TAG, blockEntityTag)
        }
    }
}