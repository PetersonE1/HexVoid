package org.agent.hexvoid.blocks.portal_mapper

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.level.block.state.BlockState
import org.agent.hexvoid.blocks.base.BaseContainer
import org.agent.hexvoid.blocks.base.ContainerSlotDelegate
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock.Companion.ITEM_STATE
import org.agent.hexvoid.registry.HexvoidBlockEntities

class PortalMapperBlockEntity(pos: BlockPos, state: BlockState) : HexBlockEntity(
    HexvoidBlockEntities.PORTAL_MAPPER.value, pos, state),
    BaseContainer, ADIotaHolder
{
    override val stacks = BaseContainer.withSize(1)

    var iotaStack by ContainerSlotDelegate(0)

    private val iotaHolder get() = IXplatAbstractions.INSTANCE.findDataHolder(iotaStack)

    override fun loadModData(tag: CompoundTag) {
        stacks.clear()
        ContainerHelper.loadAllItems(tag, stacks)
    }

    override fun saveModData(tag: CompoundTag) {
        ContainerHelper.saveAllItems(tag, stacks)
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
            iotaTag == null -> PortalMapperItemState.EMPTY
            iotaTag.getString("hexcasting:type") == "hexcasting:null" -> PortalMapperItemState.FULL_NULL
            iotaTag.getString("hexcasting:type") == "hexvoid:reality_scent" -> PortalMapperItemState.FULL_SCENT
            else -> PortalMapperItemState.EMPTY
        }
        level?.setBlockAndUpdate(blockPos, blockState.setValue(ITEM_STATE, value))
    }
}