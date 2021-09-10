package org.alberto97.hisenseair.ui.preferences

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@ExperimentalMaterialApi
@Composable
fun DialogPreference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: @Composable (() -> Unit)? = null,
    dialog: @Composable (closeDialog: () -> Unit) -> Unit,
) {
    val (showDeleteAlert, setShowDeleteAlert) = remember { mutableStateOf(false) }
    if (showDeleteAlert)
        dialog { setShowDeleteAlert(false) }

    Preference(
        title = title,
        modifier = modifier,
        summary = summary,
        icon = icon,
        onClick = { setShowDeleteAlert(true) }
    )
}