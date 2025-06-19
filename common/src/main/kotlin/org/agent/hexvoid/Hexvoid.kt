package org.agent.hexvoid

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.agent.hexvoid.config.HexvoidConfig
import org.agent.hexvoid.networking.HexvoidNetworking
import org.agent.hexvoid.registry.HexvoidActions
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.HexvoidCreativeTabs
import org.agent.hexvoid.registry.HexvoidIotaTypes
import org.agent.hexvoid.registry.HexvoidItems

object Hexvoid {
    const val MODID = "hexvoid"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexvoidConfig.init()
        initRegistries(
            HexvoidBlocks,
            HexvoidItems,
            HexvoidCreativeTabs,
            HexvoidActions,
            HexvoidIotaTypes
        )
        HexvoidNetworking.init()
    }
}
