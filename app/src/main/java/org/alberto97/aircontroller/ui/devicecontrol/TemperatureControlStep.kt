package org.alberto97.aircontroller.ui.devicecontrol

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.ui.theme.AppTheme

@Composable
fun TemperatureControlStep(
    temp: Int,
    onTempDown: () -> Unit,
    onTempUp: () -> Unit,
    onTempClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 24.dp)
    ) {

        IconButton(
            onClick = { onTempDown() },
        ) {
            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
        Text(
            text = " $temp°",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.clickable(onClick = onTempClick)
        )
        IconButton(
            onClick = { onTempUp() },
        ) {
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            TemperatureControlStep(37, {}, {}, {})
        }
    }
}