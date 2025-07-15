package org.agent.hexvoid.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvent
import org.agent.hexvoid.Hexvoid

object HexvoidSoundEvents : HexvoidRegistrar<SoundEvent>(
    Registries.SOUND_EVENT,
    { BuiltInRegistries.SOUND_EVENT }
) {
    val AMBIENT_INTERSTITIAL_WASTES_LOOP = register("ambient.interstitial_wastes.loop")

    private fun register(name: String): Entry<SoundEvent> {
        return this.register(name.replace('.', '/')) { SoundEvent.createVariableRangeEvent(Hexvoid.id(name)) }
    }
}