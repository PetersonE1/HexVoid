package org.agent.hexvoid.world.biome

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.Music
import net.minecraft.sounds.Musics
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.biome.*
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
                    .waterColor(0x150216)
                    .waterFogColor(0x130014)
                    .skyColor(0x000000)
                    .grassColorOverride(0x505050)
                    .foliageColorOverride(0x808080)
                    .fogColor(0x0a0a0a)
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    .backgroundMusic(Musics.UNDER_WATER).build()
                ).build()
        }
    }
}