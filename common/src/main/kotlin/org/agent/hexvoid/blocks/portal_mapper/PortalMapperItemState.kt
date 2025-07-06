package org.agent.hexvoid.blocks.portal_mapper

import net.minecraft.util.StringRepresentable

enum class PortalMapperItemState(val stateName: String) : StringRepresentable {
    EMPTY("empty"),
    FULL_SCENT("full_scent"),
    FULL_NULL("full_null");

    override fun getSerializedName() = this.stateName

    override fun toString(): String {
        return this.getSerializedName()
    }

    val hasItem get() = this == EMPTY
}