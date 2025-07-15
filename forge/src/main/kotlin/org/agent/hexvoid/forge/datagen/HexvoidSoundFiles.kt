package org.agent.hexvoid.forge.datagen

import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.SoundDefinition
import net.minecraftforge.common.data.SoundDefinitionsProvider
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.registry.HexvoidSoundEvents

class HexvoidSoundFiles(output: PackOutput, efh: ExistingFileHelper)
    : SoundDefinitionsProvider(output, Hexvoid.MODID, efh) {
    override fun registerSounds() {
        add(HexvoidSoundEvents.AMBIENT_INTERSTITIAL_WASTES_LOOP.value, definition()
            .subtitle("sound.hexvoid.ambient.interstitial_wastes.loop")
            .with(
                sound(HexvoidSoundEvents.AMBIENT_INTERSTITIAL_WASTES_LOOP.id)
                    .stream()
            ))
    }

}