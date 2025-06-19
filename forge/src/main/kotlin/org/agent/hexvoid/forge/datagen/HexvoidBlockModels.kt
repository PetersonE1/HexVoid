package org.agent.hexvoid.forge.datagen

import at.petrak.hexcasting.api.HexAPI
import at.petrak.paucal.api.forge.datagen.PaucalBlockStateAndModelProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.common.data.ExistingFileHelper
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.RegistrarEntry

@Suppress("SameParameterValue")
class HexvoidBlockModels(output: PackOutput, efh: ExistingFileHelper) : PaucalBlockStateAndModelProvider(output, Hexvoid.MODID, efh) {
    override fun registerStatesAndModels() {
        horizontalBlockAndItem(HexvoidBlocks.DEBUG_PORTAL) { id ->
            models()
                .cube(
                    id.path,
                    modLoc("block/debug_portal"), // down
                    modLoc("block/debug_portal"), // up
                    modLoc("block/debug_portal"), // north
                    modLoc("block/debug_portal"), // south
                    modLoc("block/debug_portal"), // east
                    modLoc("block/debug_portal"), // west
                )
                .texture("particle", modLoc("block/debug_portal"))
        }
    }

    private fun horizontalBlockAndItem(
        entry: RegistrarEntry<Block>,
        builder: (ResourceLocation) -> BlockModelBuilder,
    ) {
        val model = builder.invoke(entry.id)
        horizontalBlock(entry.value, model)
        simpleBlockItem(entry.value, model)
    }
}