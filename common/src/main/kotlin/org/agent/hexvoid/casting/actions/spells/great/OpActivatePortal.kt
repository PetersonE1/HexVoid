package org.agent.hexvoid.casting.actions.spells.great

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs
import at.petrak.hexcasting.api.casting.mishaps.MishapLocationInWrongDimension
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock
import org.agent.hexvoid.casting.iotas.RealityScentIota
import org.agent.hexvoid.registry.HexvoidBlocks
import org.agent.hexvoid.world.dimension.HexvoidDimensions
import kotlin.math.pow

object OpActivatePortal : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getBlockPos(0, argc)
        val duration = args.getInt(1, argc)
        env.assertPosInRange(target)

        val state = env.world.getBlockState(target)
        if (!state.`is`(HexvoidBlocks.PORTAL_MAPPER_FULL.block))
            throw MishapBadBlock(target, "block.hexvoid.portal_mapper_full".asTranslatedComponent)

        val blockEntity = PortalMapperBlock.getBlockEntity(env.world, target) ?: throw MishapInternalException(IllegalStateException("Block entity not found!"))
        val iota = blockEntity.readIota(env.world)
        if (iota == null)
            throw MishapBadBlock(target, "error.hexvoid.missing_iota".asTranslatedComponent)
        if (iota !is RealityScentIota && iota !is NullIota)
            throw MishapInvalidIota(iota, -1, "error.hexvoid.invalid_iota".asTranslatedComponent)

        val targetDim = when (iota) {
            is RealityScentIota -> iota.globalPos.dimension()
            is NullIota -> HexvoidDimensions.INTERSTITIA_LEVEL_KEY
            else -> env.world.dimension()
        }

        if (!HexConfig.server().canTeleportInThisDimension(targetDim) || !HexConfig.server().canTeleportInThisDimension(env.world.dimension()))
            throw MishapLocationInWrongDimension(targetDim.location())

        val baseCost = (if (targetDim == HexvoidDimensions.INTERSTITIA_LEVEL_KEY) 5 else
            (if (targetDim == env.world.dimension()) 10 else 15)) * MediaConstants.CRYSTAL_UNIT
        val durationCost = (duration / (20.0 * 60.0)).pow(2.0).toInt() * MediaConstants.CRYSTAL_UNIT

        return SpellAction.Result(
            Spell(state, duration, target),
            baseCost + durationCost,
            listOf(ParticleSpray.Companion.burst(target.center, 1.0))
        )
    }

    private data class Spell(val state: BlockState, val duration: Int, val pos: BlockPos) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            (state.block as PortalMapperBlock).activatePortal(duration, env.world, pos)
        }
    }
}