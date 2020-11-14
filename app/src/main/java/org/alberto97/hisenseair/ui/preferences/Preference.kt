package org.alberto97.hisenseair.ui.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun Preference(
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val clickModifier =
        if (onClick != null)
            modifier.clickable(onClick = onClick)
        else
            modifier

    if (summary != null) {
        ListItem(
            text = { Text(title) },
            secondaryText = { Text(summary) },
            trailing = trailing,
            icon = { renderIcon(icon) },
            modifier = clickModifier
        )
    } else {
        ListItem(
            text = { Text(title) },
            trailing = trailing,
            icon = { renderIcon(icon) },
            modifier = clickModifier
        )
    }
}

@Composable
fun renderIcon(icon: VectorAsset?) {
    if (icon != null)
        Icon(
            icon,
            modifier = Modifier.size(40.dp),
            tint = Color(0xFF757575)
        )
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface {
            Column {
                Preference("Preference")
                Preference("Preference", "I need this very long summary to test multiline text on this composable")
                Preference("Preference2", "Summary", vectorResource(R.drawable.ic_fan), onClick = {})
            }
        }
    }
}