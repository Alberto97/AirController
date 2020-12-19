package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun PreferenceCategory(title: String) {
    OneLinePreference(
        icon = {},
        modifier = Modifier.preferredHeightIn(min = MinHeight).padding(top = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primary)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                PreferenceCategory("Test")
            }
        }
    }
}