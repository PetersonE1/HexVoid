package org.agent.hexvoid.functionality

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.agent.hexvoid.world.dimension.HexvoidDimensions
import kotlin.math.floor

class InterstitiaTeleport {
    companion object {
        fun teleportToInterstita(player: ServerPlayer) : Boolean {
            val server = player.server!!

            val origin = player.level() as ServerLevel
            val interstitia = server.allLevels.find { level -> level.dimension().location() == HexvoidDimensions.INTERSTITIA_LEVEL_KEY.location() }

            if (interstitia == null)
                return false

            val pos = player.onPos
            var altitude = 321.0
            while (interstitia.getBlockState(BlockPos(pos.x, altitude.toInt(), pos.z)).isAir) {
                altitude--
                if (altitude < -64) {
                    altitude = 321.0
                    break
                }
            }
            val compressionFactor = origin.dimensionType().coordinateScale
            var destPos = Vec3(floor(pos.x * compressionFactor) + 0.5, altitude + 1, floor(pos.z * compressionFactor) + 0.5)
            val border = interstitia.worldBorder

            destPos = border.clampToBounds(destPos.x, destPos.y, destPos.z).center

            player.teleportTo(interstitia, destPos.x, destPos.y, destPos.z, player.yRot, player.xRot)
            player.onUpdateAbilities()
            if (altitude >= 321) {
                player.addEffect(MobEffectInstance(MobEffects.SLOW_FALLING, 1200))
            }

            return true
        }

        fun teleport(player: ServerPlayer, globalPos: GlobalPos?) : Boolean {
            val server = player.server!!
            if (globalPos == null) {
                return teleportToInterstita(player)
            }

            val destination = server.allLevels.find { level -> level.dimension().location() == globalPos.dimension().location() } ?: server.overworld()
            val destPos = globalPos.pos().center

            player.teleportTo(destination, destPos.x, destPos.y, destPos.z, player.yRot, player.xRot)
            player.onUpdateAbilities()

            return true
        }
    }
}