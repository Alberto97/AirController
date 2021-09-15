package org.alberto97.hisenseair.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun BottomSheetListItem(text: String, icon: @Composable (() -> Unit), selected: Boolean, onClick: () -> Unit) {
    ListItem (
        icon = icon,
        text = {
            Text(
                text,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
        },
        trailing = { DoneIcon(selected) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun DoneIcon(show: Boolean) {
    if (show)
        Icon(
            Icons.Rounded.Done,
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            BottomSheetListItem(
                text = "Test",
                icon = { Icon(Icons.Default.Air, null) },
                selected = true,
                onClick = {}
            )
        }
    }
}