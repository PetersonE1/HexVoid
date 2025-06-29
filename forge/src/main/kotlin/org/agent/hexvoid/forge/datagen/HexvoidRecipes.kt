package org.agent.hexvoid.forge.datagen

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.EntityTagIngredient
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.EntityTypeIngredient
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.VillagerIngredient
import at.petrak.hexcasting.datagen.HexAdvancements
import at.petrak.hexcasting.datagen.recipe.builders.BrainsweepRecipeBuilder
import at.petrak.paucal.api.datagen.PaucalRecipeProvider
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraftforge.common.Tags
import org.agent.hexvoid.registry.HexvoidBlocks
import java.util.function.Consumer

class HexvoidRecipes(out: PackOutput, modid: String) : PaucalRecipeProvider(out, modid) {
    override fun buildRecipes(recipes: Consumer<FinishedRecipe>) {
        val enlightenment = HexAdvancements.ENLIGHTEN;

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_EMPTY.block),
            VillagerIngredient(VillagerProfession.CARTOGRAPHER, null, 5),
            HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("brainsweep/portal_mapper_empty_to_cartographer"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_EMPTY.block),
            EntityTypeIngredient(EntityType.SNIFFER),
            HexvoidBlocks.PORTAL_MAPPER_SNIFFER.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("brainsweep/portal_mapper_empty_to_sniffer"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.block),
            EntityTypeIngredient(EntityType.SNIFFER),
            HexvoidBlocks.PORTAL_MAPPER_FULL.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("brainsweep/portal_mapper_cartographer_to_full"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_SNIFFER.block),
        VillagerIngredient(VillagerProfession.CARTOGRAPHER, null, 5),
            HexvoidBlocks.PORTAL_MAPPER_FULL.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("brainsweep/portal_mapper_sniffer_to_full"))
    }

}