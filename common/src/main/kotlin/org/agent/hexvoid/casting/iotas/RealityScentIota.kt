package org.agent.hexvoid.casting.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.darkPurple
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level

class RealityScentIota(val globalPos: GlobalPos) : Iota(TYPE, globalPos) {
    override fun isTruthy(): Boolean = true
    override fun toleratesOther(that: Iota): Boolean = typesMatch(this, that) && this.payload == (that as RealityScentIota).payload

    override fun serialize(): CompoundTag {
        val tag = CompoundTag()
        tag.putIntArray("pos", listOf(globalPos.pos().x, globalPos.pos().y, globalPos.pos().z))
        tag.putString("dimNamespace", globalPos.dimension().location().namespace)
        tag.putString("dimPath", globalPos.dimension().location().path)
        return tag
    }

    companion object {
        val TYPE = object : IotaType<RealityScentIota>() {
            override fun deserialize(tag: Tag, world: ServerLevel): RealityScentIota {
                tag as CompoundTag
                val dim: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, ResourceLocation(tag.getString("dimNamespace"), tag.getString("dimPath")))
                val xyz = tag.getIntArray("pos")
                val pos = BlockPos(xyz[0], xyz[1], xyz[2])
                val globalPos = GlobalPos.of(dim, pos)

                return RealityScentIota(globalPos)
            }

            override fun display(tag: Tag): Component {
                tag as CompoundTag
                val xyz = tag.getIntArray("pos")

                return "${tag.getString("dimPath").uppercase()} (${xyz[0]}, ${xyz[1]}, ${xyz[2]})".asTranslatedComponent.darkPurple
            }

            override fun color(): Int = 0xad1074
        }
    }
}