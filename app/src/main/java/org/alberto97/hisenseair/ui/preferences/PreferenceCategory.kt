package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun PreferenceCategory(title: String) {
    Box(modifier = Modifier.padding(top = 16.dp).height(32.dp)) {
        ListItem(icon = {}) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = MaterialTheme.colors.primary
                )
            )
        }
    }
}

@Preview
@Composable
@ExperimentalMaterialApi
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                PreferenceCategory("Test")
            }
        }
    }
}