package org.agent.hexvoid.blocks.portal_mapper

import net.minecraft.util.StringRepresentable

enum class PortalMapperItemState(val stateName: String) : StringRepresentable {
    EMPTY("empty"),
    SCENT("scent"),
    NULL("null"),
    INVALID("invalid");

    override fun getSerializedName() = this.stateName

    override fun toString(): String {
        return this.getSerializedName()
    }

    val hasItem get() = this == EMPTY
}