package org.alberto97.hisenseair.ui.preferences

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun Preference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: Painter? = null,
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
fun RenderIcon(icon: Painter?) {
    if (icon != null)
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF757575)
        )
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                Preference("Preference")
                Preference("Preference", summary = "I need this very long summary to test multiline text on this composable")
                Preference("Preference2", summary = "Summary", icon = painterResource(R.drawable.ic_fan), onClick = {})
            }
        }
    }
}