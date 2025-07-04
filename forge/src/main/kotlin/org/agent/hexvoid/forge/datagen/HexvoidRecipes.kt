package org.agent.hexvoid.forge.datagen

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.EntityTagIngredient
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.EntityTypeIngredient
import at.petrak.hexcasting.common.recipe.ingredient.brainsweep.VillagerIngredient
import at.petrak.hexcasting.datagen.HexAdvancements
import at.petrak.hexcasting.datagen.recipe.builders.BrainsweepRecipeBuilder
import at.petrak.paucal.api.datagen.PaucalRecipeProvider
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.advancements.packs.VanillaNetherAdvancements
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.server.PlayerAdvancements
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.Tags
import org.agent.hexvoid.registry.HexvoidBlocks
import java.util.function.Consumer

class HexvoidRecipes(out: PackOutput, modid: String) : PaucalRecipeProvider(out, modid) {
    override fun buildRecipes(recipes: Consumer<FinishedRecipe>) {
        val enlightenment = HexAdvancements.ENLIGHTEN;

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(HexvoidBlocks.INTERSTITIAL_COBBLESTONE.item), RecipeCategory.BUILDING_BLOCKS,
            HexvoidBlocks.INTERSTITIAL_STONE.item, 0.1f, 160)
            .unlockedBy("has_item", hasItem(HexvoidBlocks.INTERSTITIAL_COBBLESTONE.item))
            .save(recipes)

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HexvoidBlocks.PORTAL_MAPPER_EMPTY.item)
            .pattern("sss")
            .pattern("qaq")
            .pattern("qeq")
            .define('s', HexBlocks.SLATE_BLOCK)
            .define('q', HexvoidBlocks.QUARTZ_INFUSED_STONE.item)
            .define('a', HexItems.CHARGED_AMETHYST)
            .define('e', Items.ENDER_PEARL)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes)

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HexvoidBlocks.QUARTZ_INFUSED_STONE.item)
            .pattern("sss")
            .pattern("sqs")
            .pattern("sss")
            .define('s', Blocks.STONE)
            .define('q', Items.QUARTZ)
            .unlockedBy("has_item", hasItem(Items.QUARTZ))
            .save(recipes)


        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_EMPTY.block),
            VillagerIngredient(VillagerProfession.CARTOGRAPHER, null, 5),
            HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("portal_mapper_empty_to_cartographer"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_EMPTY.block),
            EntityTypeIngredient(EntityType.SNIFFER),
            HexvoidBlocks.PORTAL_MAPPER_SNIFFER.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("portal_mapper_empty_to_sniffer"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_CARTOGRAPHER.block),
            EntityTypeIngredient(EntityType.SNIFFER),
            HexvoidBlocks.PORTAL_MAPPER_FULL.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("portal_mapper_cartographer_to_full"))

        BrainsweepRecipeBuilder(StateIngredientHelper.of(HexvoidBlocks.PORTAL_MAPPER_SNIFFER.block),
        VillagerIngredient(VillagerProfession.CARTOGRAPHER, null, 5),
            HexvoidBlocks.PORTAL_MAPPER_FULL.block.defaultBlockState(), MediaConstants.CRYSTAL_UNIT * 10)
            .unlockedBy("enlightenment", enlightenment)
            .save(recipes, modLoc("portal_mapper_sniffer_to_full"))
    }

}