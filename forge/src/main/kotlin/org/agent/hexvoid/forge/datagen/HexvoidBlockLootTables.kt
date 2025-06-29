package org.agent.hexvoid.forge.datagen

import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import org.agent.hexvoid.registry.HexvoidBlocks

class HexvoidBlockLootTables : BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS) {
    override fun generate() {
        dropSelf(HexvoidBlocks.DEBUG_PORTAL.value)
        dropSelf(HexvoidBlocks.INTERSTITIAL_STONE.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_EMPTY.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_SNIFFER.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_FULL.value)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return HexvoidBlocks.entries.map { it.value }.toMutableList()
    }
}