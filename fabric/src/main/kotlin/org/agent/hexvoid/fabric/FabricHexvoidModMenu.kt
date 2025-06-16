package org.agent.hexvoid.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import org.agent.hexvoid.HexvoidClient

object FabricHexvoidModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(HexvoidClient::getConfigScreen)
}
