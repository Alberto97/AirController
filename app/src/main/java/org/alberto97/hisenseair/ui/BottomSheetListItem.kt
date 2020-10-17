package org.alberto97.hisenseair.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun <T> BottomSheetListItem(id: T, text: String, icon: Int, selected: Boolean, onClick: (data: T) -> Unit) {
    ListItem (
        icon = { leadingIcon(icon) },
        text = {
            Text(
                text,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
        },
        trailing = { doneIcon(selected) },
        modifier = Modifier.clickable(onClick = { onClick(id) })
    )
}

@Composable
private fun leadingIcon(icon: Int?) {
    if (icon != null)
        Icon(
            asset = vectorResource(icon),
            tint = Color.Gray
        )
}

@Composable
private fun doneIcon(show: Boolean) {
    if (show)
        Icon(
            Icons.Rounded.Done,
            tint = MaterialTheme.colors.primary
        )
}

@Preview
@Composable
private fun preview() {
    AppTheme(darkTheme = true) {
        Surface {
            BottomSheetListItem(WorkMode.Auto, "Test", R.drawable.ic_fan, true, {})
        }
    }
}