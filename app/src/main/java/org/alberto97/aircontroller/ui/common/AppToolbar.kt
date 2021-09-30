package org.alberto97.aircontroller.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

@Composable
fun ExtendedTopAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues = AppBarDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        modifier.height(ExtendedAppBarHeight),
        backgroundColor,
        contentColor,
        elevation,
        contentPadding,
        content
    )
}

// Per material design specs: https://material.io/components/app-bars-top#specs
private val ExtendedAppBarHeight = 128.dp