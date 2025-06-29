package org.agent.hexvoid.forge.datagen

import at.petrak.paucal.api.forge.datagen.PaucalBlockStateAndModelProvider
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.RegistrarEntry
import java.util.function.Function

@Suppress("SameParameterValue")
class HexvoidBlockModels(output: PackOutput, efh: ExistingFileHelper) : PaucalBlockStateAndModelProvider(output, Hexvoid.MODID, efh) {
    override fun registerStatesAndModels() {
        easyHorizontalBlockAndItem(HexvoidBlocks.DEBUG_PORTAL)
        easyBlockAndItem(HexvoidBlocks.INTERSTITIAL_STONE, true)
        easyBlockAndItem(HexvoidBlocks.INTERSTITIAL_COBBLESTONE)
        easyHorizontalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_EMPTY)
        easyHorizontalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER)
        easyHorizontalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_SNIFFER)
        easyHorizontalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_FULL)
        easyBlockAndItem(HexvoidBlocks.QUARTZ_INFUSED_STONE, true)
    }

    private fun easyHorizontalBlockAndItem(entry: RegistrarEntry<Block>) {
        val name = entry.id.path
        try {
            val texture = modLoc("block/${name}")
            horizontalBlockAndItem(entry) {
                models()
                    .cube(
                        name,
                        texture,
                        texture,
                        texture,
                        texture,
                        texture,
                        texture
                    ).texture("particle", texture)
            }
        } catch (e: IllegalArgumentException) {
            horizontalBlockAndItem(entry) {
                models()
                    .cube(
                        name,
                        modLoc("block/${name}/down"),
                        modLoc("block/${name}/up"),
                        modLoc("block/${name}/north"),
                        modLoc("block/${name}/south"),
                        modLoc("block/${name}/east"),
                        modLoc("block/${name}/west"),
                    ).texture("particle", modLoc("block/${name}/north"))
            }
        }
    }

    private fun easyBlockAndItem(entry: RegistrarEntry<Block>, randomRot: Boolean = false) {
        val name = entry.id.path
        try {
            val texture = modLoc("block/${name}")
            blockAndItem(entry, randomRot) {
                models()
                    .cube(
                        name,
                        texture,
                        texture,
                        texture,
                        texture,
                        texture,
                        texture
                    ).texture("particle", texture)
            }
        } catch (e: IllegalArgumentException) {
            blockAndItem(entry, randomRot) {
                models()
                    .cube(
                        name,
                        modLoc("block/${name}/down"),
                        modLoc("block/${name}/up"),
                        modLoc("block/${name}/north"),
                        modLoc("block/${name}/south"),
                        modLoc("block/${name}/east"),
                        modLoc("block/${name}/west"),
                    ).texture("particle", modLoc("block/${name}/north"))
            }
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

    private fun blockAndItem(
        entry: RegistrarEntry<Block>,
        randomRot: Boolean,
        builder: (ResourceLocation) -> BlockModelBuilder,
    ) {
        val model = builder.invoke(entry.id)
        if (randomRot) {
            randomRotBlock(entry.value, model)
            simpleBlockItem(entry.value, model)
        } else {
            simpleBlockWithItem(entry.value, model)
        }
    }

    private fun randomRotBlock(block: Block, model: ModelFile) {
        getVariantBuilder(block)
            .partialState()
            .modelForState()
            .modelFile(model)

            .nextModel()
            .modelFile(model)
            .rotationY(90)

            .nextModel()
            .modelFile(model)
            .rotationX(180)

            .nextModel()
            .modelFile(model)
            .rotationY(270)
            .addModel()
    }
}