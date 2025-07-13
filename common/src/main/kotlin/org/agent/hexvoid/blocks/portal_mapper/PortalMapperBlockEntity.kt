package org.agent.hexvoid.blocks.portal_mapper

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.getCompound
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.blockEntity.TickableBlockEntity
import org.agent.hexvoid.blocks.base.BaseContainer
import org.agent.hexvoid.blocks.base.ContainerSlotDelegate
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock.Companion.ITEM_STATE
import org.agent.hexvoid.registry.HexvoidBlockEntities

class PortalMapperBlockEntity(pos: BlockPos, state: BlockState) : HexBlockEntity(
    HexvoidBlockEntities.PORTAL_MAPPER.value, pos, state),
    BaseContainer, ADIotaHolder, TickableBlockEntity
{
    override val stacks = BaseContainer.withSize(1)

    var iotaStack by ContainerSlotDelegate(0)

    private val iotaHolder get() = IXplatAbstractions.INSTANCE.findDataHolder(iotaStack)

    var portalActive: Boolean = false
        private set

    var deactivationTime: Long = 0
        private set

    fun activatePortal(deactivation: Long) {
        portalActive = true
        deactivationTime = deactivation
    }

    fun deactivatePortal() {
        portalActive = false
    }

    override fun loadModData(tag: CompoundTag) {
        stacks.clear()
        portalActive = tag.getBoolean("active")
        deactivationTime = tag.getLong("deactivationTime")
        ContainerHelper.loadAllItems(tag.getCompound("container"), stacks)
    }

    override fun saveModData(tag: CompoundTag) {
        tag.putBoolean("active", portalActive)
        tag.putLong("deactivationTime", deactivationTime)
        val itemTag = CompoundTag()
        ContainerHelper.saveAllItems(itemTag, stacks)
        tag.putCompound("container", itemTag)
    }

    override fun readIotaTag() = iotaHolder?.readIotaTag()

    override fun writeIota(iota: Iota?, simulate: Boolean): Boolean {
        val success = iotaHolder?.writeIota(iota, simulate) ?: false
        if (!simulate && success)
            sync()
        return success
    }

    override fun writeable() = iotaHolder?.writeable() ?: false

    override fun setChanged() {
        super.setChanged()
        val iotaTag = readIotaTag()
        val value = when {
            iotaTag == null -> if (iotaHolder == null) PortalMapperItemState.EMPTY else PortalMapperItemState.INVALID
            iotaTag.getString("hexcasting:type") == "hexcasting:null" -> PortalMapperItemState.NULL
            iotaTag.getString("hexcasting:type") == "hexvoid:reality_scent" -> PortalMapperItemState.SCENT
            else -> PortalMapperItemState.INVALID
        }
        level?.setBlockAndUpdate(blockPos, blockState.setValue(ITEM_STATE, value))
    }

    override fun tick() {
        if (portalActive && (level?.gameTime ?: 0) >= deactivationTime)
            deactivatePortal()
    }
}