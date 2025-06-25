package org.agent.hexvoid.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import org.agent.hexvoid.casting.iotas.RealityScentIota

object HexvoidIotaTypes : HexvoidRegistrar<IotaType<*>>(
    HexRegistries.IOTA_TYPE,
    { HexIotaTypes.REGISTRY }
) {
    val REALITY_SCENT = register("reality_scent") { RealityScentIota.TYPE }
}