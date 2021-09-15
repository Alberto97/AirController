package org.alberto97.hisenseair.models

import androidx.annotation.DrawableRes


class ListItemModel<T>(
    val value: T,
    val label: String,
    @DrawableRes val resourceDrawable: Int,
    val selected: Boolean = false
)