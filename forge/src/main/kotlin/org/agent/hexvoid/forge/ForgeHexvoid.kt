package org.agent.hexvoid.forge

import dev.architectury.platform.forge.EventBuses
import org.agent.hexvoid.Hexvoid
import net.minecraft.data.DataProvider
import net.minecraft.data.DataProvider.Factory
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.fml.common.Mod
import org.agent.hexvoid.forge.datagen.HexvoidBlockLootTables
import org.agent.hexvoid.forge.datagen.HexvoidBlockModels
import org.agent.hexvoid.forge.datagen.HexvoidItemModels
import org.agent.hexvoid.forge.datagen.HexvoidRecipes
import org.agent.hexvoid.forge.datagen.HexvoidWorldGenProvider
import org.agent.hexvoid.forge.datagen.tags.HexvoidBlockTagProvider
import org.agent.hexvoid.forge.datagen.tags.HexvoidFluidTagProvider
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Hexvoid.MODID)
class HexvoidForge {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Hexvoid.MODID, this)
            addListener(ForgeHexvoidClient::init)
            addListener(::gatherData)
        }
        Hexvoid.init()
    }

    private fun gatherData(event: GatherDataEvent) {
        event.apply {
            val efh = existingFileHelper
            addProvider(includeServer()) { HexvoidWorldGenProvider(it, lookupProvider) }
            addProvider(includeClient()) { HexvoidBlockModels(it, efh) }
            addProvider(includeClient()) { HexvoidItemModels(it, efh) }
            //addProvider(includeClient()) { ItemModelBuilders(it, efh) }
            addProvider(includeServer()) {
                LootTableProvider(it, setOf(), listOf(
                    LootTableProvider.SubProviderEntry(::HexvoidBlockLootTables, LootContextParamSets.BLOCK)
                ))
            }
            addProvider(includeServer()) { HexvoidRecipes(it, Hexvoid.MODID) }
            addProvider(includeServer()) { HexvoidBlockTagProvider(it, lookupProvider) }
            addProvider(includeServer()) { HexvoidFluidTagProvider(it, lookupProvider, efh) }
        }
    }
}

fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run, Factory { factory(it) })
