package org.alberto97.hisenseair.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun BottomSheetContent(
    title: String,
    content: @Composable (() -> Unit)
) {
    Column {
        Text(
            title,
            modifier = Modifier.padding(16.dp)
        )
        content()
    }
}

@Composable
@ExperimentalMaterialApi
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            BottomSheetContent(title = "Title") {
                BottomSheetListItem(
                    text = "Test",
                    icon = { Icon(Icons.Default.Air, null) },
                    selected = false,
                    onClick = {}
                )
                BottomSheetListItem(
                    text = "Test",
                    icon = { Icon(Icons.Default.Air, null) },
                    selected = true,
                    onClick = {}
                )
            }
        }
    }
}