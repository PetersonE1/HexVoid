@file:JvmName("HexvoidAbstractions")

package org.agent.hexvoid

import dev.architectury.injectables.annotations.ExpectPlatform
import org.agent.hexvoid.registry.HexvoidRegistrar

fun initRegistries(vararg registries: HexvoidRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: HexvoidRegistrar<T>) {
    throw AssertionError()
}
