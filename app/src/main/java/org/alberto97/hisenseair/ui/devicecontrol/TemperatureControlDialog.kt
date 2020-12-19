package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme
import java.util.*

@Composable
fun TemperatureControlDialog(
    temp: Float,
    min: Float,
    max: Float,
    onOk: (value: Float) -> Unit,
    onCancel: () -> Unit
) {
    val state = remember { mutableStateOf(temp) }
    Surface {
        Column(
            Modifier.padding(6.dp)
        ) {
            Column(
                Modifier.padding(horizontal = 36.dp)
                    .padding(top = 36.dp, bottom = 20.dp)
            ) {
                Text(
                    text = " ${state.value.toInt()}°",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding()
                )
                Slider(
                    value = state.value,
                    onValueChange = { state.value = it },
                    valueRange = min..max
                )
            }
            Row(
                Modifier.align(Alignment.End)
            ) {
                TextButton({ onCancel() }) {
                    Text(
                        text = stringResource(android.R.string.cancel)
                            .toUpperCase(Locale.getDefault())
                    )
                }

                TextButton({ onOk(state.value) }) {
                    Text(
                        text = stringResource(android.R.string.ok)
                            .toUpperCase(Locale.getDefault())
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        TemperatureControlDialog(30f, 16f, 60f, {}, {})
    }
}