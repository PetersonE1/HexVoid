package org.agent.hexvoid.registry

import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlockEntity

object HexvoidBlockEntities : HexvoidRegistrar<BlockEntityType<*>>(
    Registries.BLOCK_ENTITY_TYPE,
    { BuiltInRegistries.BLOCK_ENTITY_TYPE }
) {
    @JvmField
    val PORTAL_MAPPER = register("portal_mapper", ::PortalMapperBlockEntity) {
        arrayOf(HexvoidBlocks.PORTAL_MAPPER_EMPTY.value, HexvoidBlocks.PORTAL_MAPPER_FULL.value,
            HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.value, HexvoidBlocks.PORTAL_MAPPER_SNIFFER.value)
    }

    private fun <T : BlockEntity> register(
        name: String,
        func: (BlockPos, BlockState) -> T,
        blocks: () -> Array<Block>,
    ) = register(name) { IXplatAbstractions.INSTANCE.createBlockEntityType(func, *blocks()) }
}