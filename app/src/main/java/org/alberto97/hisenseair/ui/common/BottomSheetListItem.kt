package org.alberto97.hisenseair.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun <T> BottomSheetListItem(id: T, text: String, icon: Int, selected: Boolean, onClick: (data: T) -> Unit) {
    ListItem (
        icon = { LeadingIcon(icon) },
        text = {
            Text(
                text,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
        },
        trailing = { DoneIcon(selected) },
        modifier = Modifier.clickable(onClick = { onClick(id) })
    )
}

@Composable
private fun LeadingIcon(icon: Int?) {
    if (icon != null)
        Icon(
            painterResource(icon),
            contentDescription = null,
            tint = Color.Gray
        )
}

@Composable
private fun DoneIcon(show: Boolean) {
    if (show)
        Icon(
            Icons.Rounded.Done,
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme(darkTheme = true) {
        Surface {
            BottomSheetListItem("", "Test", R.drawable.ic_fan, true, {})
        }
    }
}