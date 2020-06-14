package org.alberto97.hisenseair.models

abstract class ListItemBase<T: Enum<*>> {
    abstract val id: T
    abstract var current: Boolean
}