package org.agent.hexvoid.networking

import dev.architectury.networking.NetworkChannel
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.networking.msg.HexvoidMessageCompanion

object HexvoidNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Hexvoid.id("networking_channel"))

    fun init() {
        for (subclass in HexvoidMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
