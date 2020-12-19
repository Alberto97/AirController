package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun SwitchPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    summary: String? = null,
    icon: VectorAsset? = null,
) {
    Preference(
        title = title,
        summary = summary,
        icon = icon,
        onClick = { onCheckedChange(!checked) },
        trailing = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                SwitchPreference("Title", true, {})
                SwitchPreference(
                    "Power",
                    true,
                    {},
                    "Turn off the device",
                    vectorResource(R.drawable.round_power_settings_new_24)
                )
            }
        }
    }
}