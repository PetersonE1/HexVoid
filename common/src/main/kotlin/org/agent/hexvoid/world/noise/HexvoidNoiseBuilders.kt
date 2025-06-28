package org.agent.hexvoid.world.noise

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseRouterData
import net.minecraft.world.level.levelgen.NoiseSettings
import net.minecraft.world.level.levelgen.SurfaceRules
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidBlocks


class HexvoidNoiseBuilders {
    companion object {
        val STONE: SurfaceRules.RuleSource = SurfaceRules.state(
            HexvoidBlocks.INTERSTITIAL_STONE.block.defaultBlockState()
        )

        fun interstitiaNoiseSettings(
            densityFunctions: HolderGetter<DensityFunction>,
            noise: HolderGetter<NormalNoise.NoiseParameters>
        ): NoiseGeneratorSettings {
            val interstitial_stone: BlockState =
                HexvoidBlocks.INTERSTITIAL_STONE.block.defaultBlockState()
            return NoiseGeneratorSettings(
                NoiseSettings(0, 128, 2, 1),  // noiseSettings
                interstitial_stone,  // defaultBlock
                Blocks.WATER.defaultBlockState(),  // defaultFluid
                makeNoiseRouter(densityFunctions, noise),  // noiseRouter
                interstitiaSurfaceRules(),  // surfaceRule
                mutableListOf<Climate.ParameterPoint?>(),  // spawnTarget
                -64,  // seaLevel
                false,  // disableMobGeneration
                false,  // aquifersEnabled
                false,  // oreVeinsEnabled
                false // useLegacyRandomSource
            )
        }

        fun interstitiaSurfaceRules(): SurfaceRules.RuleSource {
            return SurfaceRules.sequence(STONE)
        }

        private fun makeNoiseRouter(
            densityFunctions: HolderGetter<DensityFunction>,
            noise: HolderGetter<NormalNoise.NoiseParameters>
        ): NoiseRouter {
            return NoiseRouterData.noNewCaves(densityFunctions, noise, buildFinalDensity(densityFunctions))
        }

        private fun buildFinalDensity(densityFunctions: HolderGetter<DensityFunction>): DensityFunction {
            var density = getFunction(
                densityFunctions,
                ResourceKey.create(
                    Registries.DENSITY_FUNCTION,
                    ResourceLocation(Hexvoid.MODID, "base_3d_noise_hexvoid")
                )
            )
            density = DensityFunctions.add(density, DensityFunctions.constant(0.53))
            density = NoiseRouterData.slide(density, 0, 128, 72, 0, -0.2, 8, 40, -0.1)
            density = DensityFunctions.add(density, DensityFunctions.constant(-0.05))
            density = DensityFunctions.blendDensity(density)
            density = DensityFunctions.interpolated(density)

            density = DensityFunctions.add(density, DensityFunctions.constant(-0.25))
            density = DensityFunctions.blendDensity(density)

            density = density.squeeze()
            return density
        }

        private fun getFunction(
            densityFunctions: HolderGetter<DensityFunction>,
            key: ResourceKey<DensityFunction>
        ): DensityFunction {
            return DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key))
        }
    }
}