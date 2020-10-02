package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
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

        Box(
            modifier = Modifier.size(40.dp)
                .clickable(onClick = onTempDown)
                .constrainAs(buttonLeft) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(text.start)
                }
        ) {
            Icon(
                asset = vectorResource(R.drawable.ic_round_chevron_left_24),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.Center)
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
        Box(
            modifier =  Modifier.size(40.dp)
                .clickable(onClick = onTempUp)
                .constrainAs(buttonRight) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(text.end)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                asset = vectorResource(R.drawable.ic_round_chevron_right_24),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface {
            TemperatureControlStep(37, {}, {}, {})
        }
    }
}