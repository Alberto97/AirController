package org.alberto97.aircontroller.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.common.AppToolbar
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutScreen(
    navigateUp: () -> Unit,
    viewModel: AboutViewModel = getViewModel()
) {
    val appVersion = viewModel.appVersion

    AboutScreen(
        appVersion = appVersion,
        onBackClick = navigateUp,
        openRepository = { viewModel.openRepository() }
    )
}

@ExperimentalMaterialApi
@Composable
private fun AboutScreen(
    appVersion: String,
    onBackClick: () -> Unit,
    openRepository: () -> Unit
) {
    Scaffold(topBar = {
        AppToolbar(
            title = { Text(stringResource(R.string.about_title)) },
            navigateUp = { onBackClick() }
        )
    }) {
        Column {
            ListItem(
                icon = { Icon(painterResource(R.drawable.ic_github), null)},
                text = { Text(stringResource(R.string.about_repository_title)) },
                secondaryText = { Text(stringResource(R.string.about_repository_summary)) },
                modifier = Modifier.clickable { openRepository() }
            )
            Divider(color = Color.LightGray)
            ListItem(
                icon = {},
                text = {
                    Text(
                        stringResource(R.string.about_category_info),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            )
            ListItem(
                icon = {},
                text = { Text(stringResource(R.string.about_version)) },
                secondaryText = { Text(appVersion) }
            )
            ListItem(
                icon = {},
                text = { Text(stringResource(R.string.about_author)) },
                secondaryText = { Text(stringResource(R.string.about_author_fulfilling_ego)) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        AboutScreen({ })
    }
}
