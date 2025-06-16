package org.agent.hexvoid

import org.agent.hexvoid.config.HexvoidConfig
import org.agent.hexvoid.config.HexvoidConfig.GlobalConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object HexvoidClient {
    fun init() {
        HexvoidConfig.initClient()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(GlobalConfig::class.java, parent).get()
    }
}
