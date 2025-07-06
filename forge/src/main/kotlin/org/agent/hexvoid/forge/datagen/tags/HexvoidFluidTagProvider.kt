package org.agent.hexvoid.forge.datagen.tags

import at.petrak.paucal.api.datagen.PaucalBlockTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.data.ExistingFileHelper
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.registry.HexvoidFluids
import org.agent.hexvoid.tags.HexvoidFluidTags
import java.util.concurrent.CompletableFuture

class HexvoidFluidTagProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>,
    efh: ExistingFileHelper
) : TagsProvider<Fluid>(output, Registries.FLUID, registries, Hexvoid.MODID, efh) {
    override fun addTags(provider: HolderLookup.Provider) {
        add(tag(HexvoidFluidTags.LIQUID_QUARTZ),
            HexvoidFluids.LIQUID_QUARTZ.value, HexvoidFluids.LIQUID_QUARTZ_FLOWING.value)
    }

    fun add(appender: TagAppender<Fluid>, vararg fluids: Fluid) {
        for (fluid in fluids) {
            appender.add(BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow())
        }
    }
}