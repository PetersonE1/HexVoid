package org.agent.hexvoid.world.noise

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.synth.BlendedNoise
import org.agent.hexvoid.Hexvoid

class HexvoidDensityFunctions {
    companion object {
        val BASE_3D_NOISE_HEXVOID: ResourceKey<DensityFunction> = ResourceKey.create(Registries.DENSITY_FUNCTION,
            Hexvoid.id("base_3d_noise_hexvoid"))

        fun boostrap(context: BootstapContext<DensityFunction>) {
            context.register(BASE_3D_NOISE_HEXVOID, BlendedNoise.createUnseeded(
                0.25, // xz scale
                0.25, // y scale
                80.0, // xz factor
                40.0, // y factor
                4.0)) // smear scale multiplier, capped at 8
        }
    }
}