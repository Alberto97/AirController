package org.alberto97.hisenseair.ui.preferences

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun Preference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    @SuppressLint("ComposableLambdaParameterNaming") trailing: @Composable (() -> Unit)? = null,
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
            icon = { RenderIcon(icon) },
            modifier = clickModifier
        )
    } else {
        ListItem(
            text = { Text(title) },
            trailing = trailing,
            icon = { RenderIcon(icon) },
            modifier = clickModifier
        )
    }
}

@Composable
fun RenderIcon(icon: ImageVector?) {
    if (icon != null)
        Icon(
            icon,
            modifier = Modifier.size(40.dp),
            tint = Color(0xFF757575)
        )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                Preference("Preference")
                Preference("Preference", summary = "I need this very long summary to test multiline text on this composable")
                Preference("Preference2", summary = "Summary", icon = vectorResource(R.drawable.ic_fan), onClick = {})
            }
        }
    }
}