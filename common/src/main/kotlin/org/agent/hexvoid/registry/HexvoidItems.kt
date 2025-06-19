package org.agent.hexvoid.registry

import dev.architectury.registry.item.ItemPropertiesRegistry
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import org.agent.hexvoid.items.base.ItemPredicateProvider

object HexvoidItems : HexvoidRegistrar<Item>(Registries.ITEM, { BuiltInRegistries.ITEM }) {
    val props: Item.Properties = Item.Properties().`arch$tab`(HexvoidCreativeTabs.HEX_VOID.key)

    private val unstackable get() = props.stacksTo(1)

    private fun Properties.noTab() = this.`arch$tab`(null as CreativeModeTab?)

    override fun initClient() {
        registerItemProperties()
    }

    private fun registerItemProperties() {
        for (entry in entries) {
            (entry.value as? ItemPredicateProvider)?.getModelPredicates()?.forEach {
                ItemPropertiesRegistry.register(entry.value, it.id, it.predicate)
            }
        }
    }
}