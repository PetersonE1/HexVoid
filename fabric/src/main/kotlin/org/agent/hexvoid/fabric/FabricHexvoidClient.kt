package org.agent.hexvoid.fabric

import org.agent.hexvoid.HexvoidClient
import net.fabricmc.api.ClientModInitializer

object FabricHexvoidClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexvoidClient.init()
    }
}
