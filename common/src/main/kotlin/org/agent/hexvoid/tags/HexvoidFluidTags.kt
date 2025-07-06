package org.agent.hexvoid.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.FluidTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidRegistrar

class HexvoidFluidTags {
    companion object {
        val LIQUID_QUARTZ: TagKey<Fluid> = TagKey.create(Registries.FLUID, Hexvoid.id("liquid_quartz"))
    }
}