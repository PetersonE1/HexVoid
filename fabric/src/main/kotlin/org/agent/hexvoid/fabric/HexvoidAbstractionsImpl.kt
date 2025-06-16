@file:JvmName("HexvoidAbstractionsImpl")

package org.agent.hexvoid.fabric

import org.agent.hexvoid.registry.HexvoidRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: HexvoidRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
