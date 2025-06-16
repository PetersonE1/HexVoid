package org.agent.hexvoid.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.networking.HexvoidNetworking
import org.agent.hexvoid.networking.handler.applyOnClient
import org.agent.hexvoid.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface HexvoidMessage

sealed interface HexvoidMessageC2S : HexvoidMessage {
    fun sendToServer() {
        HexvoidNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface HexvoidMessageS2C : HexvoidMessage {
    fun sendToPlayer(player: ServerPlayer) {
        HexvoidNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        HexvoidNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface HexvoidMessageCompanion<T : HexvoidMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Hexvoid.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is HexvoidMessageC2S -> msg.applyOnServer(ctx)
                    else -> Hexvoid.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Hexvoid.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is HexvoidMessageS2C -> msg.applyOnClient(ctx)
                    else -> Hexvoid.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
