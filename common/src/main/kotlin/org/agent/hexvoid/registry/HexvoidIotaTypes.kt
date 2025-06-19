package org.agent.hexvoid.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import org.agent.hexvoid.casting.iotas.RealityFlavorIota

object HexvoidIotaTypes : HexvoidRegistrar<IotaType<*>>(
    HexRegistries.IOTA_TYPE,
    { HexIotaTypes.REGISTRY }
) {
    val REALITY_FLAVOR = register("reality_flavor") { RealityFlavorIota.TYPE }
}