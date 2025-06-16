package org.agent.hexvoid.fabric

import org.agent.hexvoid.Hexvoid
import net.fabricmc.api.ModInitializer

object FabricHexvoid : ModInitializer {
    override fun onInitialize() {
        Hexvoid.init()
    }
}
