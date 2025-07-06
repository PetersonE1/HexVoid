package org.agent.hexvoid.blocks.base

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import kotlin.reflect.KProperty

/** Taken from [HexDebug](https://github.com/object-Object/HexDebug/blob/382a024d15955277f629f0cd39670f6d54126643/Common/src/main/kotlin/gay/object/hexdebug/blocks/base/ContainerSlotDelegate.kt). */
data class ContainerSlotDelegate(val slot: Int) {
    operator fun getValue(container: Container, property: KProperty<*>): ItemStack = container.getItem(slot)

    operator fun setValue(container: Container, property: KProperty<*>, stack: ItemStack) {
        container.setItem(slot, stack)
    }
}