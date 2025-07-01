package org.agent.hexvoid.forge.datagen

import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.client.model.generators.ModelBuilder
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.minecraftforge.common.data.ExistingFileHelper
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidFluids
import org.agent.hexvoid.registry.HexvoidItems

class HexvoidItemModels(output: PackOutput, efh: ExistingFileHelper) : ItemModelProvider(output, Hexvoid.MODID, efh) {
    override fun registerModels() {
        liquidQuartzBucket(HexvoidItems.LIQUID_QUARTZ_BUCKET.id)
    }

    private fun liquidQuartzBucket(item: ResourceLocation) {
        basicItem(item)
            .parent(getExistingFile(mcLoc("item/bucket")))
    }
}

fun <T : ModelBuilder<T>> T.layers(start: Int, vararg layers: String?): T {
    var index = start
    for (layer in layers) {
        if (layer != null) {
            texture("layer$index", layer)
            index += 1
        }
    }
    return this
}