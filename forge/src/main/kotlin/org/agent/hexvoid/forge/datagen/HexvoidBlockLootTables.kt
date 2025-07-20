package org.agent.hexvoid.forge.datagen

import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import org.agent.hexvoid.registry.HexvoidBlocks

class HexvoidBlockLootTables : BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS) {
    override fun generate() {
        dropSelf(HexvoidBlocks.DEBUG_PORTAL.value)
        dropOther(HexvoidBlocks.INTERSTITIAL_STONE.value, HexvoidBlocks.INTERSTITIAL_COBBLESTONE.item)
        dropSelf(HexvoidBlocks.INTERSTITIAL_COBBLESTONE.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_EMPTY.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_SNIFFER.value)
        dropSelf(HexvoidBlocks.PORTAL_MAPPER_FULL.value)
        dropSelf(HexvoidBlocks.QUARTZ_INFUSED_STONE.value)
        dropSelf(HexvoidBlocks.LIQUID_QUARTZ_BLOCK.value) // this doesn't make sense to me and something must be wrong, but datagen won't run without a loot pool
        dropSelf(HexvoidBlocks.CARNIVOROUS_LOG.value)
        dropSelf(HexvoidBlocks.CRYSTAL.value)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return HexvoidBlocks.entries.map { it.value }.toMutableList()
    }
}