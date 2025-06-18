package org.agent.hexvoid.world.dimension

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.BiomeSources
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import org.agent.hexvoid.Hexvoid
import java.util.OptionalLong

class HexvoidDimensions {
    companion object {
        val HEXVOIDDIM_KEY: ResourceKey<LevelStem> = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation(Hexvoid.MODID, "hexvoiddim"))
        val HEXVOIDDIM_LEVEL_KEY: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation(Hexvoid.MODID, "hexvoiddim"))
        val HEXVOIDDIM_TYPE: ResourceKey<DimensionType> = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation(Hexvoid.MODID, "hexvoiddim_type"))

        @JvmStatic
        fun bootstrapType(context: BootstapContext<DimensionType>) {
            context.register(HEXVOIDDIM_TYPE, DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm (evaporates water)
                false, // natural (if true, portals spawn zombified piglins; if false, compasses and clocks spin randomly)
                1.0, // coordinateScale
                false, // bedWorks
                true, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight (portals can't spawn, and chorus fruit won't teleport above this height)
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

        /*@JvmStatic
        fun bootstrapLevelStem(context: BootstapContext<LevelStem>) {
            context.register(HEXVOIDDIM_KEY, LevelStem(context.lookup(Registries.DIMENSION_TYPE).getOrThrow(HEXVOIDDIM_TYPE), ChunkGenerator(

            )))
        }*/
    }
}