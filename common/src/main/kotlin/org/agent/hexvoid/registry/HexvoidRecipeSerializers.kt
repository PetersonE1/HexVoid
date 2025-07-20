package org.agent.hexvoid.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.recipe.DestabilizeRecipe

object HexvoidRecipeSerializers : HexvoidRegistrar<RecipeSerializer<*>>(Registries.RECIPE_SERIALIZER, { BuiltInRegistries.RECIPE_SERIALIZER }) {
    @JvmField
    val DESTABILIZE = register("destabilize") { DestabilizeRecipe.Companion.Serializer() }
}