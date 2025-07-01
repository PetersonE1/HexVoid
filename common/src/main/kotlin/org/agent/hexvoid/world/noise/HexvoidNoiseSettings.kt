package org.agent.hexvoid.world.noise

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import org.agent.hexvoid.Hexvoid

class HexvoidNoiseSettings {
    companion object {
        val INTERSTITIA_NOISE: ResourceKey<NoiseGeneratorSettings> = ResourceKey.create(Registries.NOISE_SETTINGS,
            Hexvoid.id("interstitia_noise")
        )

        @JvmStatic
        fun bootstrap(context: BootstapContext<NoiseGeneratorSettings>) {
            val densityFunctions = context.lookup(Registries.DENSITY_FUNCTION)
            val noise = context.lookup(Registries.NOISE)
            context.register(INTERSTITIA_NOISE, HexvoidNoiseBuilders.interstitiaNoiseSettings(densityFunctions, noise))
        }
    }
}