package org.agent.hexvoid.functionality

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.RelativeMovement
import net.minecraft.world.entity.projectile.ThrownEnderpearl
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity
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

        fun teleportPlayer(player: ServerPlayer, globalPos: GlobalPos?) : Boolean {
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

        fun teleportEntity(entity: Entity, globalPos: GlobalPos?, server: MinecraftServer) : Boolean {
            if (globalPos == null) {
                return teleportEntityToInterstitia(entity, server)
            }

            val destination = server.allLevels.find { level -> level.dimension().location() == globalPos.dimension().location() } ?: server.overworld()
            val destPos = globalPos.pos().center

            return entity.teleportTo(destination, destPos.x, destPos.y, destPos.z, RelativeMovement.ALL, entity.yRot, entity.xRot)
        }

        fun teleportEntityToInterstitia(entity: Entity, server: MinecraftServer): Boolean {
            val origin = entity.level() as ServerLevel
            val interstitia = server.allLevels.find { level -> level.dimension().location() == HexvoidDimensions.INTERSTITIA_LEVEL_KEY.location() }

            if (interstitia == null)
                return false

            val pos = entity.onPos
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

            return entity.teleportTo(interstitia, destPos.x, destPos.y, destPos.z, RelativeMovement.ALL, entity.yRot, entity.xRot)
        }

        fun teleport(entity: Entity, globalPos: GlobalPos?, level: ServerLevel) : Boolean {
            if (entity is ServerPlayer) {
                return teleportPlayer(entity, globalPos)
            }

            if (entity is ThrownEnderpearl) {
                val newEntity = entity.owner
                if (newEntity != null)
                    entity.discard()
                if (newEntity is ServerPlayer)
                    return teleportPlayer(newEntity, globalPos)
            }
            val newEntity = entity.rootVehicle

            return teleportEntity(newEntity, globalPos, level.server)
        }
    }
}