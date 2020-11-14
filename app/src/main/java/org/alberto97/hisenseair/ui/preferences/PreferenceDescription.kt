package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.preferredHeightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun PreferenceDescription(
    icon: VectorAsset? = null,
    text: String
) {
    OneLinePreference(
        icon = { renderIcon(icon) },
        modifier = Modifier.preferredHeightIn(min = MinHeightWithIcon)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.body2.copy(color = Color(0xFF757575))
        )
    }
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface {
            PreferenceDescription(vectorResource(id = R.drawable.ic_thermometer), "Test")
        }
    }
}