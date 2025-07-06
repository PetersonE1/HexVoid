package org.agent.hexvoid.utils

import at.petrak.hexcasting.api.utils.italic
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.enums.enumEntries
import kotlin.math.ceil

val ItemStack.styledHoverName: Component
    get() = Component.empty()
    .append(hoverName)
    .withStyle(rarity.color)
    .also { if (hasCustomHoverName()) it.italic }

@OptIn(ExperimentalStdlibApi::class)
inline val <reified T : Enum<T>> T.asItemPredicate get() =
    ordinal.toFloat() / (ceil(enumEntries<T>().lastIndex.toFloat() / 2f) * 2f)

val Boolean.asItemPredicate get() = if (this) 1f else 0f