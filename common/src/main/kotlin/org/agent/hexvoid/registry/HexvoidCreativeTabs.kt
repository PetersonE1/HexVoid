package org.agent.hexvoid.registry

import dev.architectury.registry.CreativeTabRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items

object HexvoidCreativeTabs : HexvoidRegistrar<CreativeModeTab>(
    Registries.CREATIVE_MODE_TAB,
    { BuiltInRegistries.CREATIVE_MODE_TAB },
) {
    val HEX_VOID = make("hexvoid") {
        icon { HexvoidBlocks.DEBUG_PORTAL.item.defaultInstance }
        displayItems { _, output ->
            output.accept(HexvoidBlocks.DEBUG_PORTAL.item.defaultInstance)
        }
    }

    @Suppress("SameParameterValue")
    private fun make(name: String, action: CreativeModeTab.Builder.() -> Unit) = register(name) {
        CreativeTabRegistry.create { builder ->
            builder.title(Component.translatable("itemGroup.$name"))
            action.invoke(builder)
        }
    }
}