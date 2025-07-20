package org.agent.hexvoid.forge.datagen

import at.petrak.paucal.api.forge.datagen.PaucalBlockStateAndModelProvider
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.blocks.crystal.CrystalBlock
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperItemState
import org.agent.hexvoid.items.PortalMapperBlockItem
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.RegistrarEntry
import org.agent.hexvoid.utils.asItemPredicate

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
        easyAxisBlockAndItem(HexvoidBlocks.CARNIVOROUS_LOG)
        crystalBlockAndItem(HexvoidBlocks.CRYSTAL)
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

    private fun easyAxisBlockAndItem(entry: RegistrarEntry<Block>) {
        val block = entry.value as RotatedPillarBlock
        val side = modLoc("block/${entry.id.path}/side")
        val end = modLoc("block/${entry.id.path}/end")
        axisBlockAndItem(block, side, end)
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

    private fun axisBlockAndItem(
        block: RotatedPillarBlock,
        side: ResourceLocation,
        end: ResourceLocation,
    ) {
        val verticalModel = models().cubeColumn(name(block), side, end)
        axisBlock(
            block,
            verticalModel,
            models().cubeColumnHorizontal(name(block) + "_horizontal", side, end)
        )
        simpleBlockItem(block, verticalModel)
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
                        modLoc("block/${path}/west"),
                    ).texture("particle", modLoc("block/${path}/north"))

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

    private fun crystalBlockAndItem(entry: RegistrarEntry<Block>) {
        getVariantBuilder(entry.value).also { builder ->
            val path = entry.id.path
            for (state in listOf(true, false)) {
                val stateName = if (state) "sheen" else "dull"
                val model = models()
                    .cubeAll("${path}_$stateName", modLoc("block/${path}/$stateName"))
                    .texture("particle", modLoc("block/${path}/$stateName"))
                builder.partialState()
                    .with(CrystalBlock.SHEEN, state)
                    .modelForState()
                    .modelFile(model)
                    .addModel()
                if (state) {
                    simpleBlockItem(entry.value, model)
                }
            }
        }
    }

    private fun name(block: Block): String? {
        return ForgeRegistries.BLOCKS.getKey(block)?.path
    }
}