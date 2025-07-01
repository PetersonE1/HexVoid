package org.agent.hexvoid.fabric

import org.agent.hexvoid.HexvoidClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.resources.ResourceLocation
import org.agent.hexvoid.registry.HexvoidFluids

object FabricHexvoidClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexvoidClient.init()

        FluidRenderHandlerRegistry.INSTANCE.register(HexvoidFluids.LIQUID_QUARTZ.value, HexvoidFluids.LIQUID_QUARTZ_FLOWING.value,
            SimpleFluidRenderHandler(
                ResourceLocation("minecraft:block/water_still"),
                ResourceLocation("minecraft:block/water_flow"),
                0xFFFFFFFF.toInt()
            ))

        BlockRenderLayerMap.INSTANCE.putFluids(
            RenderType.translucent(),
            HexvoidFluids.LIQUID_QUARTZ.value, HexvoidFluids.LIQUID_QUARTZ_FLOWING.value)
    }
}
