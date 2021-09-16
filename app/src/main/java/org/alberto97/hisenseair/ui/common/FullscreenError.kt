package org.alberto97.hisenseair.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun FullscreenError(tryAgain: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.CloudOff,
                contentDescription = null,
                tint = MaterialTheme.colors.primary.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
            Text(
                text = stringResource(R.string.error_message),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = tryAgain, modifier = Modifier
                    .padding(8.dp)
            ) {
                Text("Try Again")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme(darkTheme = false) {
        Surface {
            FullscreenError({})
        }
    }
}