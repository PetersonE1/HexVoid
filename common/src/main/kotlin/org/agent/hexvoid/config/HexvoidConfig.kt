package org.agent.hexvoid.config

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.common.PlayerEvent
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject
import me.shedaniel.autoconfig.serializer.PartitioningSerializer
import me.shedaniel.autoconfig.serializer.PartitioningSerializer.GlobalData
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.minecraft.network.FriendlyByteBuf
import org.agent.hexvoid.Hexvoid
import org.agent.hexvoid.networking.msg.MsgSyncConfigS2C

object HexvoidConfig {
    @JvmStatic
    lateinit var holder: ConfigHolder<GlobalConfig>

    @JvmStatic
    val client get() = holder.config.client

    @JvmStatic
    val server get() = syncedServerConfig ?: holder.config.server

    // only used on the client, probably
    private var syncedServerConfig: ServerConfig? = null

    fun init() {
        holder = AutoConfig.register(
            GlobalConfig::class.java,
            PartitioningSerializer.wrap(::Toml4jConfigSerializer),
        )

        PlayerEvent.PLAYER_JOIN.register { player ->
            MsgSyncConfigS2C(holder.config.server).sendToPlayer(player)
        }
    }

    fun initClient() {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register { _ ->
            syncedServerConfig = null
        }
    }

    fun onSyncConfig(serverConfig: ServerConfig) {
        syncedServerConfig = serverConfig
    }

    @Config(name = Hexvoid.MODID)
    class GlobalConfig : GlobalData() {
        @Category("client")
        @TransitiveObject
        val client = ClientConfig()

        @Category("server")
        @TransitiveObject
        val server = ServerConfig()
    }

    @Config(name = "client")
    class ClientConfig : ConfigData {
        @Tooltip
        val clientConfigOption: Boolean = true
    }

    @Config(name = "server")
    class ServerConfig : ConfigData {
        @Tooltip
        var portalRecursionDepth: Int = 512
            private set

        @Tooltip
        var portalLeapDistance: Int = 3
            private set

        fun encode(buf: FriendlyByteBuf) {
            buf.writeInt(portalRecursionDepth)
            buf.writeInt(portalLeapDistance)
        }

        companion object {
            fun decode(buf: FriendlyByteBuf) = ServerConfig().apply {
                portalRecursionDepth = buf.readInt()
                portalLeapDistance = buf.readInt()
            }
        }
    }
}
