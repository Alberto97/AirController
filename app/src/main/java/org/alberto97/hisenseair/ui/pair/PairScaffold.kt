package org.alberto97.hisenseair.ui.pair

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.common.HiSnackbar

@Composable
fun PairScaffold(
    title: String,
    subtitle: String,
    message: String? = null,
    onClearMessage: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable () -> Unit
) {
    HiSnackbar(
        scaffoldState = scaffoldState,
        message = message,
        onClearMessage = onClearMessage
    )

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(
                text = subtitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            content()
        }
    }
}