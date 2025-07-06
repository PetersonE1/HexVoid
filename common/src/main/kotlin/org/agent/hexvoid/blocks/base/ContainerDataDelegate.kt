package org.agent.hexvoid.blocks.base

import net.minecraft.world.inventory.ContainerData
import kotlin.reflect.KProperty

/** Taken from [HexDebug](https://github.com/object-Object/HexDebug/blob/382a024d15955277f629f0cd39670f6d54126643/Common/src/main/kotlin/gay/object/hexdebug/blocks/base/ContainerDataDelegate.kt). */
data class ContainerDataLongDelegate(
    val data: ContainerData,
    val index0: Int,
    val index1: Int,
    val index2: Int,
    val index3: Int,
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return (
                data.get(index0).toUShort().toLong()
                        + (data.get(index1).toUShort().toLong() shl 16)
                        + (data.get(index2).toUShort().toLong() shl 32)
                        + (data.get(index3).toUShort().toLong() shl 48)
                )
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        data.set(index0, value.toInt())
        data.set(index1, (value shr 16).toInt())
        data.set(index2, (value shr 32).toInt())
        data.set(index3, (value shr 48).toInt())
    }
}