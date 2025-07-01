package org.agent.hexvoid.registry

import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import org.agent.hexvoid.blocks.fluids.liquid_quartz.LiquidQuartz

object HexvoidFluids : HexvoidRegistrar<Fluid>(
    Registries.FLUID,
    { BuiltInRegistries.FLUID }
) {
    @JvmField
    val LIQUID_QUARTZ = register("liquid_quartz") { LiquidQuartz.STILL_FLUID }
    @JvmField
    val LIQUID_QUARTZ_FLOWING = register("liquid_quartz_flowing") { LiquidQuartz.FLOWING_FLUID }
}
