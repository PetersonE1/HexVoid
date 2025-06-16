package org.agent.hexvoid.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import org.agent.hexvoid.config.HexvoidConfig
import org.agent.hexvoid.networking.msg.*

fun HexvoidMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            HexvoidConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
