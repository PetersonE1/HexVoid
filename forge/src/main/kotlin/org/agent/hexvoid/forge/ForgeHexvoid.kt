package org.agent.hexvoid.forge

import dev.architectury.platform.forge.EventBuses
import org.agent.hexvoid.Hexvoid
import net.minecraft.data.DataProvider
import net.minecraft.data.DataProvider.Factory
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.fml.common.Mod
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
            // TODO: add datagen providers here
        }
    }
}

fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run, Factory { factory(it) })
