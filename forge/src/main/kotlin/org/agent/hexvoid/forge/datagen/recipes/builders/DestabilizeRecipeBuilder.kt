package org.agent.hexvoid.forge.datagen.recipes.builders

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredient
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.registry.HexvoidRecipeSerializers
import java.util.function.Consumer

class DestabilizeRecipeBuilder(
    val blockIn: StateIngredient,
    val result: BlockState,
    val mediaCost: Long
) : RecipeBuilder {
    private val advancement: Advancement.Builder = Advancement.Builder.advancement()

    override fun unlockedBy(
        criterionName: String,
        criterionTrigger: CriterionTriggerInstance
    ): RecipeBuilder {
        this.advancement.addCriterion(criterionName, criterionTrigger)
        return this
    }

    override fun group(groupName: String?): RecipeBuilder {
        return this
    }

    override fun getResult(): Item {
        return this.result.block.asItem()
    }

    override fun save(
        finishedRecipeConsumer: Consumer<FinishedRecipe>,
        recipeId: ResourceLocation
    ) {
        if (this.advancement.criteria.isEmpty()) {
            throw IllegalStateException("No way of obtaining recipe $recipeId")
        }

        this.advancement.parent(ResourceLocation("recipes/root"))
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(RequirementsStrategy.OR)

        finishedRecipeConsumer.accept(Result(
            recipeId,
            this.blockIn, this.mediaCost, this.result,
            this.advancement,
            ResourceLocation(recipeId.namespace, "recipes/destabilize/${recipeId.path}")
        ))
    }

    @JvmRecord
    data class Result(val id: ResourceLocation, val blockIn: StateIngredient, val mediaCost: Long,
                      val result: BlockState, val advancement: Advancement.Builder, val advancementId: ResourceLocation) : FinishedRecipe {
        override fun serializeRecipeData(json: JsonObject) {
            json.add("blockIn", this.blockIn.serialize())
            json.addProperty("cost", this.mediaCost)
            json.add("result", StateIngredientHelper.serializeBlockState(this.result))
        }

        override fun getId(): ResourceLocation {
            return this.id
        }

        override fun getType(): RecipeSerializer<*> {
            return HexvoidRecipeSerializers.DESTABILIZE.value
        }

        override fun serializeAdvancement(): JsonObject? {
            return this.advancement.serializeToJson()
        }

        override fun getAdvancementId(): ResourceLocation? {
            return this.advancementId
        }
    }
}