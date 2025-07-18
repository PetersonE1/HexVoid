package org.agent.hexvoid.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.material.PushReaction
import org.agent.hexvoid.blocks.debug_portal.DebugPortalBlock
import org.agent.hexvoid.blocks.debug_portal.HorizontalBlock
import org.agent.hexvoid.blocks.fluids.liquid_quartz.LiquidQuartzBlock
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock
import org.agent.hexvoid.items.base.PortalMapperBlockItem
import net.minecraft.world.item.Item.Properties as ItemProperties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as BlockProperties

object HexvoidBlocks : HexvoidRegistrar<Block>(Registries.BLOCK, { BuiltInRegistries.BLOCK }) {
    @JvmField
    val DEBUG_PORTAL = blockItem("debug_portal", HexvoidItems.props) {
        DebugPortalBlock(BlockProperties.copy(Blocks.BEDROCK))
    }

    @JvmField
    val INTERSTITIAL_STONE = blockItem("interstitial_stone", HexvoidItems.props) {
        Block(BlockProperties.copy(Blocks.STONE))
    }

    @JvmField
    val INTERSTITIAL_COBBLESTONE = blockItem("interstitial_cobblestone", HexvoidItems.props) {
        Block(BlockProperties.copy(Blocks.COBBLESTONE))
    }

    @JvmField
    val PORTAL_MAPPER_EMPTY = blockItem(
        "portal_mapper_empty",
        blockBuilder = { PortalMapperBlock(BlockProperties.copy(Blocks.STONE)) },
        itemBuilder = { PortalMapperBlockItem(it, HexvoidItems.props) }
    )

    @JvmField
    val PORTAL_MAPPER_CARTOGRAPHER = blockItem(
        "portal_mapper_cartographer",
        blockBuilder = { PortalMapperBlock(BlockProperties.copy(Blocks.STONE).lightLevel{5}) },
        itemBuilder = { PortalMapperBlockItem(it, HexvoidItems.props) }
    )

    @JvmField
    val PORTAL_MAPPER_SNIFFER = blockItem(
        "portal_mapper_sniffer",
        blockBuilder = { PortalMapperBlock(BlockProperties.copy(Blocks.STONE).lightLevel{5}) },
        itemBuilder = { PortalMapperBlockItem(it, HexvoidItems.props) }
    )

    @JvmField
    val PORTAL_MAPPER_FULL = blockItem(
        "portal_mapper_full",
        blockBuilder = { PortalMapperBlock(BlockProperties.copy(Blocks.STONE).lightLevel{10}) },
        itemBuilder = { PortalMapperBlockItem(it, HexvoidItems.props) }
    )

    @JvmField
    val QUARTZ_INFUSED_STONE = blockItem("quartz_infused_stone", HexvoidItems.props) {
        Block(BlockProperties.copy(Blocks.STONE))
    }

    @JvmField
    val LIQUID_QUARTZ_BLOCK = register("liquid_quartz") { LiquidQuartzBlock.INSTANCE }

    @JvmField
    val CARNIVOROUS_LOG = blockItem("carnivorous_log", HexvoidItems.props) {
        RotatedPillarBlock(BlockProperties.copy(Blocks.OAK_LOG))
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