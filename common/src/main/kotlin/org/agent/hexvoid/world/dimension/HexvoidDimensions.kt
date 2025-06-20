package org.agent.hexvoid.world.dimension

import com.mojang.datafixers.util.Pair
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import org.agent.hexvoid.Hexvoid
import java.util.OptionalLong

class HexvoidDimensions {
    companion object {
        val INTERSTITIA_KEY: ResourceKey<LevelStem> = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation(Hexvoid.MODID, "interstitia"))
        val INTERSTITIA_LEVEL_KEY: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation(Hexvoid.MODID, "interstitia"))
        val INTERSTITIA_TYPE: ResourceKey<DimensionType> = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation(Hexvoid.MODID, "interstitia_type"))

        @JvmStatic
        fun bootstrapType(context: BootstapContext<DimensionType>) {
            context.register(INTERSTITIA_TYPE, DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm (evaporates water)
                false, // natural (if true, portals spawn zombified piglins; if false, compasses and clocks spin randomly)
                1.0, // coordinateScale
                false, // bedWorks
                true, // respawnAnchorWorks
                -64, // minY
                320, // height
                320, // logicalHeight (portals can't spawn, and chorus fruit won't teleport above this height)
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn (what blocks infinitely burn)
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation (sky effects)
                1.0f, // ambientLight
                DimensionType.MonsterSettings(
                    false, // piglinSafe
                    false, // hasRaids
                    UniformInt.of(0, 0), // monsterSpawnLightTest
                    0 // monsterSpawnBlockLightLimit
                )
            ))
        }

        @JvmStatic
        fun bootstrapLevelStem(context: BootstapContext<LevelStem>) {
            context.register(INTERSTITIA_KEY, LevelStem(context.lookup(Registries.DIMENSION_TYPE).getOrThrow(INTERSTITIA_TYPE), NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(Climate.ParameterList(listOf(
                    Pair(
                        Climate.parameters(0.2f, 0.1f, 0.15f, 0.0f, -0.05f, 0.0f, 0.0f),
                        context.lookup(Registries.BIOME).getOrThrow(Biomes.PLAINS)),
                    Pair(
                        Climate.parameters(0.15f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
                        context.lookup(Registries.BIOME).getOrThrow(Biomes.OCEAN)
                    )
                ))),
                context.lookup(Registries.NOISE_SETTINGS).getOrThrow(NoiseGeneratorSettings.OVERWORLD)
            )))
        }
    }
}