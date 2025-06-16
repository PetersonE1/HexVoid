package org.agent.hexvoid.forge

import org.agent.hexvoid.HexvoidClient
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

object ForgeHexvoidClient {
    fun init(event: FMLClientSetupEvent) {
        HexvoidClient.init()
        LOADING_CONTEXT.registerExtensionPoint(ConfigScreenFactory::class.java) {
            ConfigScreenFactory { _, parent -> HexvoidClient.getConfigScreen(parent) }
        }
    }
}
