package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun PreferenceDescription(
    icon: @Composable (() -> Unit)? = null,
    text: String
) {
    ListItem(
        icon = icon,
        modifier = Modifier.widthIn(min = 72.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.body2.copy(color = Color(0xFF757575))
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PreferenceDescription(
                icon = {PreferenceIcon(Icons.Outlined.Thermostat)},
                text = "Test"
            )
        }
    }
}