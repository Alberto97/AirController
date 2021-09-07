package org.alberto97.hisenseair.ui.region

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.repositories.Region
import org.alberto97.hisenseair.ui.Screen
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.RegionViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun RegionScreen(
    navController: NavController,
    viewModel: RegionViewModel = getViewModel()
) {

    fun click(region: Region) {
        viewModel.setRegion(region)
        navController.navigate(Screen.Login.route)
    }
    
    RegionScreen(click = { click(it) })
}

@ExperimentalMaterialApi
@Composable
fun RegionScreen(click: (region: Region) -> Unit) {

    AppTheme {
        Scaffold(topBar = { TopAppBar (title = { Text("Pick Region") }) }) {
            Column {
                ListItem(
                    modifier = Modifier.clickable { click(Region.EU) },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_eu),
                            contentDescription = null
                        )
                    },
                    text = { Text("Europe") }
                )
                ListItem(
                    modifier = Modifier.clickable { click(Region.US) },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_us),
                            contentDescription = null
                        )
                    },
                    text = { Text("United States") }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            RegionScreen(click = {})
        }
    }
}