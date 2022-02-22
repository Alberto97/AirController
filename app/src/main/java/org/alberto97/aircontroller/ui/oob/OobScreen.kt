package org.alberto97.aircontroller.ui.oob

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.common.AppScaffold
import org.alberto97.aircontroller.ui.common.ExtendedTopAppBar
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun OobScreen(openLogin: () -> Unit, viewModel: OobViewModel = getViewModel()) {
    OobScreen(openLogin)
}

@Composable
private fun OobScreen(openLogin: () -> Unit) {
    AppScaffold(
        topBar = {
            ExtendedTopAppBar {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    val color =
                        MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primarySurface)
                    Text(
                        stringResource(R.string.oob_title),
                        color = color,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        stringResource(R.string.oob_usage_restrictions),
                        color = color,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    stringResource(R.string.oob_requirements_title),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    stringResource(R.string.oob_requirements_1),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    stringResource(R.string.oob_requirements_2),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Icon(
                painterResource(id = R.drawable.ic_weather_windy),
                contentDescription = null,
                tint = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Button(
                onClick = openLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Composable
@Preview("Light")
private fun PreviewLight() {
    AppTheme {
        OobScreen({})
    }
}

@Composable
@Preview("Dark")
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        OobScreen({})
    }
}