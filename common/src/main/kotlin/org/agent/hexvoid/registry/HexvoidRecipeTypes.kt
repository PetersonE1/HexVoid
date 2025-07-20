package org.agent.hexvoid.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.recipe.DestabilizeRecipe

object HexvoidRecipeTypes : HexvoidRegistrar<RecipeType<*>>(Registries.RECIPE_TYPE, { BuiltInRegistries.RECIPE_TYPE }) {
    @JvmField
    val DESTABILIZE_TYPE: RecipeType<DestabilizeRecipe> = registerType("destabilize")

    private fun <T: Recipe<*>> registerType(name: String): RecipeType<T> {
        val type = object: RecipeType<T> {
            override fun toString(): String {
                return Hexvoid.MODID + ":" + name
            }
        }
        return type
    }
}