package org.agent.hexvoid.forge.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.dimension.DimensionType
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.world.dimension.HexvoidDimensions
import java.util.concurrent.CompletableFuture

class HexvoidWorldGenProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>,
) : DatapackBuiltinEntriesProvider(output, registries, BUILDER, setOf(Hexvoid.MODID)) {
    companion object {
        val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, HexvoidDimensions::bootstrapType)
    }
}