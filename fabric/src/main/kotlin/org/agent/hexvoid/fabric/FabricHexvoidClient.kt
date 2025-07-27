package org.agent.hexvoid.fabric

import org.agent.hexvoid.HexvoidClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.resources.ResourceLocation
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.HexvoidFluids

object FabricHexvoidClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexvoidClient.init()

        FluidRenderHandlerRegistry.INSTANCE.register(HexvoidFluids.LIQUID_QUARTZ.value, HexvoidFluids.LIQUID_QUARTZ_FLOWING.value,
            SimpleFluidRenderHandler(
                Hexvoid.id("block/liquid_quartz/liquid_quartz_still"),
                Hexvoid.id("block/liquid_quartz/liquid_quartz_flow"),
                0xFFFFFFFF.toInt()
            ))

        BlockRenderLayerMap.INSTANCE.putFluids(
            RenderType.translucent(),
            HexvoidFluids.LIQUID_QUARTZ.value, HexvoidFluids.LIQUID_QUARTZ_FLOWING.value)

        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderType.translucent(),
            HexvoidBlocks.CRYSTAL_SHEEN.value, HexvoidBlocks.CRYSTAL_DULL.value)

        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderType.cutout(),
            HexvoidBlocks.CARNIVOROUS_SAPLING.value
        )
    }
}
