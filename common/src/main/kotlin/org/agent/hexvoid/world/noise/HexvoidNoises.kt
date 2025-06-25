package org.agent.hexvoid.world.noise

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.synth.NormalNoise
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters
import org.agent.hexvoid.Hexvoid


class HexvoidNoises {
    companion object {
        val TEMPERATURE: ResourceKey<NoiseParameters> = ResourceKey.create(Registries.NOISE,
            ResourceLocation(Hexvoid.MODID, "temperature"))
        val VEGETATION: ResourceKey<NoiseParameters> = ResourceKey.create(Registries.NOISE,
            ResourceLocation(Hexvoid.MODID, "vegetation"))

        fun boostrap(context: BootstapContext<NoiseParameters>) {
            register(context, TEMPERATURE, -8, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
            register(context, VEGETATION, -7, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
        }

        fun register(
            context: BootstapContext<NoiseParameters>,
            key: ResourceKey<NoiseParameters>,
            firstOctave: Int,
            firstAmplitude: Double,
            vararg amplitudes: Double
        ) {
            context.register(key, NoiseParameters(firstOctave, firstAmplitude, *amplitudes))
        }
    }
}