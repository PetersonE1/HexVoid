package org.agent.hexvoid.casting.actions.spells.great

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock
import org.agent.hexvoid.registry.HexvoidBlocks

object OpActivatePortal : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getBlockPos(0, argc)
        val duration = args.getInt(1, argc)
        env.assertPosInRange(target)

        val state = env.world.getBlockState(target)
        if (state.block.name != HexvoidBlocks.PORTAL_MAPPER_FULL.block.name)
            throw MishapBadBlock(target, "block.hexvoid.portal_mapper_full".asTranslatedComponent)

        // TODO: check for iota storage and mishap if invalid/no iota stored (it already won't do anything because of how the block's function works, but I want to mishap)

        return SpellAction.Result(
            Spell(state, duration, target),
            MediaConstants.DUST_UNIT, // TODO: cost calculations (costs more across dimensions, less if null, scaled by duration)
            listOf(ParticleSpray.Companion.burst(target.center, 1.0))
        )
    }

    private data class Spell(val state: BlockState, val duration: Int, val pos: BlockPos) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            (state.block as PortalMapperBlock).activatePortal(duration, env.world, pos)
        }
    }
}