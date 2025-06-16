package org.agent.hexvoid.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import org.agent.hexvoid.networking.msg.*

fun HexvoidMessageC2S.applyOnServer(ctx: PacketContext) = ctx.queue {
    // NOTE: this is commented out because otherwise it fails to compile if there's nothing inside of the when expression
    /*
    when (this) {
        // add server-side message handlers here
    }
    */
}
