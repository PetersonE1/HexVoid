package org.agent.hexvoid.recipe

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredient
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.registry.HexvoidRecipeSerializers
import org.agent.hexvoid.registry.HexvoidRecipeTypes

@JvmRecord
data class DestabilizeRecipe(
    val id: ResourceLocation,
    val blockIn: StateIngredient,
    val mediaCost: Long,
    val result: BlockState
) : Recipe<Container> {
    fun matches(blockIn: BlockState): Boolean {
        return this.blockIn.test(blockIn)
    }

    override fun getId(): ResourceLocation {
        return id
    }

    override fun getType(): RecipeType<*> {
        return HexvoidRecipeTypes.DESTABILIZE_TYPE
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return HexvoidRecipeSerializers.DESTABILIZE.value
    }

    override fun matches(container: Container, level: Level): Boolean {
        return false
    }

    override fun assemble(container: Container, registryAccess: RegistryAccess): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return false
    }

    override fun getResultItem(registryAccess: RegistryAccess): ItemStack {
        return ItemStack.EMPTY.copy()
    }

    companion object {
        class Serializer : RecipeSerializer<DestabilizeRecipe> {
            override fun fromJson(recipeId: ResourceLocation, serializedRecipe: JsonObject): DestabilizeRecipe {
                val blockIn = StateIngredientHelper.deserialize(GsonHelper.getAsJsonObject(serializedRecipe, "blockIn"))
                val cost = GsonHelper.getAsLong(serializedRecipe, "cost")
                val result = StateIngredientHelper.readBlockState(GsonHelper.getAsJsonObject(serializedRecipe, "result"))
                return DestabilizeRecipe(recipeId, blockIn, cost, result)
            }

            override fun toNetwork(buffer: FriendlyByteBuf, recipe: DestabilizeRecipe) {
                recipe.blockIn.write(buffer)
                buffer.writeVarLong(recipe.mediaCost)
                buffer.writeVarInt(Block.getId(recipe.result))
            }

            override fun fromNetwork(recipeId: ResourceLocation, buffer: FriendlyByteBuf): DestabilizeRecipe {
                val blockIn = StateIngredientHelper.read(buffer)
                val cost = buffer.readVarLong()
                val result = Block.stateById(buffer.readVarInt())
                return DestabilizeRecipe(recipeId, blockIn, cost, result)
            }
        }
    }
}
