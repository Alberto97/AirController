package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Keep synced with
// https://github.com/androidx/androidx/blob/androidx-master-dev/compose/material/material/src/commonMain/kotlin/androidx/compose/material/ListItem.kt

// List item related constants.
val MinHeight = 64.dp
val MinHeightWithIcon = 72.dp

// Icon related constants.
private val IconMinPaddedWidth = 40.dp
private val IconLeftPadding = 16.dp
private val IconVerticalPadding = 16.dp

// Content related constants.
private val ContentLeftPadding = 16.dp
private val ContentRightPadding = 16.dp

@Composable
fun OneLinePreference(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)?,
    text: @Composable (() -> Unit)
) {
    Row(modifier) {
        Box(
            Modifier.align(Alignment.CenterVertically)
                .widthIn(min = IconLeftPadding + IconMinPaddedWidth)
                .padding(
                    start = IconLeftPadding,
                    top = IconVerticalPadding,
                    bottom = IconVerticalPadding
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            if (icon != null)
                icon()
        }
        Box(
            Modifier.weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = ContentLeftPadding, end = ContentRightPadding),
            contentAlignment = Alignment.CenterStart
        ) {
            text()
        }
    }
}