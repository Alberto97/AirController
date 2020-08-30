package org.alberto97.hisenseair.models

open class BottomSheetListItem<T : Enum<*>>(
    val id: T,
    val name: String,
    val icon: Int,
    val current: Boolean
)