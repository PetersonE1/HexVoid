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
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperItemState
import org.agent.hexvoid.items.base.PortalMapperBlockItem
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.RegistrarEntry
import org.agent.hexvoid.utils.asItemPredicate
import java.util.function.Function

@Suppress("SameParameterValue")
class HexvoidBlockModels(output: PackOutput, efh: ExistingFileHelper) : PaucalBlockStateAndModelProvider(output, Hexvoid.MODID, efh) {
    override fun registerStatesAndModels() {
        easyHorizontalBlockAndItem(HexvoidBlocks.DEBUG_PORTAL)
        easyBlockAndItem(HexvoidBlocks.INTERSTITIAL_STONE, true)
        easyBlockAndItem(HexvoidBlocks.INTERSTITIAL_COBBLESTONE)
        portalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_EMPTY)
        portalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER)
        portalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_SNIFFER)
        portalBlockAndItem(HexvoidBlocks.PORTAL_MAPPER_FULL)
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
            .rotationY(180)

            .nextModel()
            .modelFile(model)
            .rotationY(270)
            .addModel()
    }

    private fun portalBlockAndItem(entry: RegistrarEntry<Block>) {
        getVariantBuilder(entry.value).also { builder ->
            val path = entry.id.path
            val itemModel = itemModels().getBuilder(path)
            for (stateName in listOf("empty", "scent", "null", "invalid")) {
                val model = models()
                    .cube(
                        "${path}_$stateName",
                        modLoc("block/${path}/down"),
                        modLoc("block/${path}/up_${stateName}"),
                        modLoc("block/${path}/north"),
                        modLoc("block/${path}/south"),
                        modLoc("block/${path}/east"),
                        modLoc("block/${path}/west"),).texture("particle", modLoc("block/${path}/north"))

                itemModel.override()
                    .predicate(PortalMapperBlockItem.ITEM_STATE, PortalMapperItemState.valueOf(stateName.uppercase()).asItemPredicate)
                    .model(model)

                for (rotation in arrayOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
                    builder
                        .partialState()
                        .with(PortalMapperBlock.ITEM_STATE, PortalMapperItemState.valueOf(stateName.uppercase()))
                        .with(PortalMapperBlock.FACING, rotation)
                        .modelForState()
                        .rotationY((rotation.toYRot().toInt() + 180) % 360)
                        .modelFile(model)
                        .addModel()
                }
            }
        }
    }
}