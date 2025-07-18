package org.agent.hexvoid.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import org.agent.hexvoid.casting.actions.spells.great.OpActivatePortal
import org.agent.hexvoid.casting.actions.spells.OpSaveScent

object HexvoidActions : HexvoidRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY },
) {
    val SAVE_SCENT = make("save_scent", HexDir.SOUTH_WEST, "eqaqewedew", OpSaveScent)
    val ACTIVATE_PORTAL = make("activate_portal", HexDir.SOUTH_WEST, "eeeeedaedeaedeaed", OpActivatePortal)

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }
}
