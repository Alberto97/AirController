package org.alberto97.hisenseair.ui.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun DeviceItem(id: String, name: String, state: String, onClick: (id: String) -> Unit) {
    ListItem(
        text = { Text(name) },
        secondaryText = { Text(state) },
        modifier = Modifier.clickable(onClick = { onClick(id) }),
        icon = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colors.primary
            ) {
                Icon(
                    asset = vectorResource(R.drawable.ic_fan),
                    tint = Color.White,
                    modifier = Modifier.padding(all = 4.dp).size(30.dp)
                )
            }
        },
    )
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface {
            DeviceItem("", "Device name", "subtitle") {}
        }
    }
}