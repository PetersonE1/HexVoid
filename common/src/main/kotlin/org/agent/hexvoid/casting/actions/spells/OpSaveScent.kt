package org.agent.hexvoid.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.core.GlobalPos
import org.agent.hexvoid.casting.iotas.RealityScentIota

object OpSaveScent : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val dim = env.world.dimension()
        val pos = env.castingEntity!!.onPos.above()
        val globalPos = GlobalPos.of(dim, pos)

        val scentIota = RealityScentIota(globalPos)

        return listOf(scentIota)
    }
}