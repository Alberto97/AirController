package org.alberto97.aircontroller.ui.devicecontrol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowserNotSupported
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.theme.AppTheme

@Composable
fun PanelUnsupported() {
    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                Icons.Filled.BrowserNotSupported,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                alpha = 0.5f,
                modifier = Modifier.padding(all = 8.dp).width(56.dp).height(56.dp)
            )
            Text(
                text = stringResource(R.string.display_in_panel_unsupported),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.display_in_panel_extend),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PanelUnsupported()
        }
    }
}