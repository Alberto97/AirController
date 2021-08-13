package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun PreferenceDescription(
    icon: Painter? = null,
    text: String
) {
    OneLinePreference(
        icon = { RenderIcon(icon) },
        modifier = Modifier.widthIn(min = MinHeightWithIcon)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.body2.copy(color = Color(0xFF757575))
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PreferenceDescription(rememberVectorPainter(Icons.Outlined.Thermostat), "Test")
        }
    }
}