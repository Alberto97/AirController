package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun SwitchPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    summary: String? = null,
    icon: @Composable (() -> Unit)? = null,
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

@ExperimentalMaterialApi
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
                    { PreferenceIcon(Icons.Rounded.PowerSettingsNew) }
                )
            }
        }
    }
}