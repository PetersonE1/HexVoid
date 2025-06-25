package org.agent.hexvoid.forge.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.dimension.DimensionType
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.world.biome.HexvoidBiomes
import org.agent.hexvoid.world.dimension.HexvoidDimensions
import org.agent.hexvoid.world.noise.HexvoidDensityFunctions
import org.agent.hexvoid.world.noise.HexvoidNoiseSettings
import org.agent.hexvoid.world.noise.HexvoidNoises
import java.util.concurrent.CompletableFuture

class HexvoidWorldGenProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>,
) : DatapackBuiltinEntriesProvider(output, registries, BUILDER, setOf(Hexvoid.MODID)) {
    companion object {
        val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, HexvoidDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, HexvoidDimensions::bootstrapLevelStem)
            .add(Registries.BIOME, HexvoidBiomes::boostrap)
            .add(Registries.DENSITY_FUNCTION, HexvoidDensityFunctions::boostrap)
            .add(Registries.NOISE, HexvoidNoises::boostrap)
            .add(Registries.NOISE_SETTINGS, HexvoidNoiseSettings::bootstrap)
    }
}