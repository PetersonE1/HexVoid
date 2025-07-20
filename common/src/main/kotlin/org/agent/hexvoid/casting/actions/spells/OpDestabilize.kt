package org.agent.hexvoid.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.recipe.DestabilizeRecipe
import org.agent.hexvoid.registry.HexvoidRecipeTypes
import org.agent.hexvoid.util.RecipeStatics

object OpDestabilize : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getBlockPos(0, argc)
        env.assertPosInRange(target)

        if (!env.canEditBlockAt(target))
            throw MishapBadLocation(target.center, "forbidden")

        val state = env.world.getBlockState(target)

        val recman = env.world.recipeManager
        val recipes = recman.getAllRecipesFor(HexvoidRecipeTypes.DESTABILIZE_TYPE)
        val recipe = recipes.find { it.matches(state) }
            ?: throw MishapBadBlock(target, "destabilizeable block".asTranslatedComponent) // todo: custom mishap type

        return SpellAction.Result(
            Spell(target, state, recipe),
            recipe.mediaCost,
            listOf(ParticleSpray.burst(target.center, 1.0))
        )
    }

    private data class Spell(val pos: BlockPos, val state: BlockState, val recipe: DestabilizeRecipe) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            env.world.setBlockAndUpdate(pos, RecipeStatics.copyProperties(state, recipe.result))
        }
    }
}