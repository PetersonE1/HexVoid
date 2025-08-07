package org.agent.hexvoid.forge.datagen.tags

import at.petrak.paucal.api.datagen.PaucalBlockTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import org.agent.hexvoid.registry.HexvoidBlocks
import java.util.concurrent.CompletableFuture

class HexvoidBlockTagProvider(packOut: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>
) : PaucalBlockTagProvider(packOut, lookupProvider) {
    override fun addTags(provider: HolderLookup.Provider) {
        add(tag(BlockTags.MINEABLE_WITH_PICKAXE),
            HexvoidBlocks.INTERSTITIAL_STONE.block, HexvoidBlocks.INTERSTITIAL_COBBLESTONE.block, HexvoidBlocks.QUARTZ_INFUSED_STONE.block,
            HexvoidBlocks.PORTAL_MAPPER_EMPTY.block, HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.block,
            HexvoidBlocks.PORTAL_MAPPER_SNIFFER.block, HexvoidBlocks.PORTAL_MAPPER_FULL.block)
        add(tag(BlockTags.MINEABLE_WITH_AXE),
            HexvoidBlocks.CARNIVOROUS_WOOD.block, HexvoidBlocks.CARNIVOROUS_CORE.block)
    }

    fun add(appender: TagAppender<Block>, vararg blocks: Block) {
        for (block in blocks) {
            appender.add(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow())
        }
    }
}