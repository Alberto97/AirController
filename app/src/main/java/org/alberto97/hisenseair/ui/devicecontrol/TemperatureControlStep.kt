package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.clickable
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
import androidx.constraintlayout.compose.ConstraintLayout
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun TemperatureControlStep(
    temp: Int,
    onTempDown: () -> Unit,
    onTempUp: () -> Unit,
    onTempClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 24.dp)
    ) {
        val (buttonLeft, buttonRight, text) = createRefs()

        IconButton(
            onClick = { onTempDown() },
            modifier = Modifier
                .constrainAs(buttonLeft) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(text.start)
                }
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
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        IconButton(
            onClick = { onTempUp() },
            modifier =  Modifier
                .constrainAs(buttonRight) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(text.end)
                    end.linkTo(parent.end)
                }
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