package org.agent.hexvoid

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.agent.hexvoid.config.HexvoidConfig
import org.agent.hexvoid.networking.HexvoidNetworking
import org.agent.hexvoid.registry.HexvoidActions
import org.agent.hexvoid.registry.HexvoidBlockEntities
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.HexvoidCreativeTabs
import org.agent.hexvoid.registry.HexvoidFluids
import org.agent.hexvoid.registry.HexvoidIotaTypes
import org.agent.hexvoid.registry.HexvoidItems
import org.agent.hexvoid.registry.HexvoidSoundEvents

object Hexvoid {
    const val MODID = "hexvoid"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexvoidConfig.init()
        initRegistries(
            HexvoidFluids,
            HexvoidBlocks,
            HexvoidBlockEntities,
            HexvoidItems,
            HexvoidCreativeTabs,
            HexvoidActions,
            HexvoidIotaTypes,
            HexvoidSoundEvents
        )
        HexvoidNetworking.init()
    }
}
