package org.alberto97.hisenseair.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun AppToolbar(
    title: @Composable () -> Unit,
    navigateUp: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    if (navigateUp == null)
        TopAppBar(title = title, actions = actions)
    else
        TopAppBar(
            title = title,
            navigationIcon = { BackNavIcon(navigateUp) },
            actions = actions
        )
}

@Composable
private fun BackNavIcon(navigateUp: () -> Unit) {
    IconButton(
        onClick = navigateUp,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )
    }
}