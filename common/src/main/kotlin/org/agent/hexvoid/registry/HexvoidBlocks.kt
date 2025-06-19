package org.agent.hexvoid.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.PushReaction
import org.agent.hexvoid.blocks.debug_portal.DebugPortalBlock
import net.minecraft.world.item.Item.Properties as ItemProperties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as BlockProperties

object HexvoidBlocks : HexvoidRegistrar<Block>(Registries.BLOCK, { BuiltInRegistries.BLOCK }) {
    @JvmField
    val DEBUG_PORTAL = blockItem("debug_portal", HexvoidItems.props) {
        DebugPortalBlock(BlockProperties.copy(Blocks.BEDROCK))
    }

    private fun BlockProperties.noPush() = pushReaction(PushReaction.BLOCK)

    private fun <T: Block> blockItem(name: String, props: ItemProperties, builder: () -> T) =
        blockItem(name, builder) { BlockItem(it, props) }

    private fun <B : Block, I : Item> blockItem(
        name: String,
        blockBuilder: () -> B,
        itemBuilder: (B) -> I,
    ): BlockItemEntry<B, I> {
        val blockEntry = register(name, blockBuilder)
        val itemEntry = HexvoidItems.register(name) { itemBuilder(blockEntry.value) }
        return BlockItemEntry(blockEntry, itemEntry)
    }

    class BlockItemEntry<B : Block, I : Item>(
        blockEntry: Entry<B>,
        val itemEntry: HexvoidRegistrar<Item>.Entry<I>
    ) : Entry<B>(blockEntry) {
        val block by ::value
        val item by itemEntry::value
    }
}