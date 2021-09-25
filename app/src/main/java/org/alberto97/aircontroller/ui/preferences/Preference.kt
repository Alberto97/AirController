package org.alberto97.aircontroller.ui.preferences

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun Preference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: @Composable (() -> Unit)? = null,
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
            icon = icon,
            modifier = clickModifier
        )
    } else {
        ListItem(
            text = { Text(title) },
            trailing = trailing,
            icon = icon,
            modifier = clickModifier
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column {
                Preference("Preference")
                Preference(
                    title = "Preference",
                    summary = "I need this very long summary to test multiline text on this composable"
                )
                Preference(
                    title = "Preference2",
                    summary = "Summary",
                    icon = { PreferenceIcon(painterResource(R.drawable.ic_fan)) },
                    onClick = {}
                )
            }
        }
    }
}