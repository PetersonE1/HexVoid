package org.agent.hexvoid.world.biome

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Music
import net.minecraft.sounds.Musics
import net.minecraft.world.level.biome.AmbientMoodSettings
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import org.agent.hexvoid.Hexvoid

class HexvoidBiomes {
    companion object {
        val INTERSTITIAL_WASTES: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME,
            Hexvoid.id("interstitial_wastes"))

        @JvmStatic
        fun boostrap(context: BootstapContext<Biome>) {
            context.register(INTERSTITIAL_WASTES, interstitialWastes(context))
        }

        fun globalOverworldGeneration(builder: BiomeGenerationSettings.Builder) {
            BiomeDefaultFeatures.addDefaultCarversAndLakes(builder)
            BiomeDefaultFeatures.addDefaultCrystalFormations(builder)
            BiomeDefaultFeatures.addDefaultOres(builder)
        }

        fun interstitialWastes(context: BootstapContext<Biome>): Biome {
            val spawnBuilder = MobSpawnSettings.Builder()

            val biomeBuilder = BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER))

            globalOverworldGeneration(biomeBuilder)

            return Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.4f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(BiomeSpecialEffects.Builder()
                    .waterColor(0xe82e3b)
                    .waterFogColor(0xbf1b26)
                    .skyColor(0x30c918)
                    .grassColorOverride(0x7f03fc)
                    .foliageColorOverride(0xd203fc)
                    .fogColor(0x22a1e6)
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    .backgroundMusic(Musics.UNDER_WATER).build()
                ).build()
        }
    }
}