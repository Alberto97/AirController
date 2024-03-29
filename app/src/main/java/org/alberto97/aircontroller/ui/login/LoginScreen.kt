package org.alberto97.aircontroller.ui.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.common.enums.Region
import org.alberto97.aircontroller.models.ScreenState
import org.alberto97.aircontroller.ui.common.AppExposedDropdownMenu
import org.alberto97.aircontroller.ui.common.AppScaffold
import org.alberto97.aircontroller.ui.common.FullscreenLoading
import org.alberto97.aircontroller.ui.common.OutlinedPasswordField
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
    openMain: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val message by viewModel.message.collectAsState()
    val region by viewModel.region.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            ScreenState.Success -> openMain()
            else -> {}
        }
    }

    LoginScreen(
        message = message,
        clearMessage = { viewModel.clearMessage() },
        state = state,
        region = region,
        setRegion = { value -> viewModel.setRegion(value) },
        regionOptions = viewModel.regions,
        onLogin = { email, password -> viewModel.login(email, password) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun LoginScreen(
    message: String,
    clearMessage: () -> Unit,
    state: ScreenState?,
    region: Region?,
    setRegion: (value: Region) -> Unit,
    regionOptions: List<Region>,
    onLogin: (email: String, password: String) -> Unit
) {
    val (password, setPassword) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }

    AppScaffold(
        message = message,
        clearMessage = clearMessage,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.login_title)) }
            )
        }
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            if (state == ScreenState.Loading)
                FullscreenLoading()
            else
                LoginContent(
                    password = password,
                    setPassword = setPassword,
                    email = email,
                    setEmail = setEmail,
                    region = region,
                    setRegion = setRegion,
                    regionOptions = regionOptions,
                    onLogin = onLogin
                )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun LoginContent(
    password: String,
    setPassword: (value: String) -> Unit,
    email: String,
    setEmail: (value: String) -> Unit,
    region: Region?,
    setRegion: (value: Region) -> Unit,
    regionOptions: List<Region>,
    onLogin: (email: String, password: String) -> Unit
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = setEmail,
            label = { Text(stringResource(R.string.email_address)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = modifier,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null
                )
            }
        )
        OutlinedPasswordField(
            value = password,
            onValueChange = setPassword,
            label = { Text(stringResource(R.string.password)) },
            modifier = modifier,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null
                )
            }
        )
        LoginDropDown(
            value = region,
            setValue = setRegion,
            options = regionOptions
        )
        LoginButton(
            enabled = email.isNotEmpty() && password.isNotEmpty() && region != null,
            onClick = { onLogin(email, password) }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun LoginDropDown(
    value: Region?,
    setValue: (value: Region) -> Unit,
    options: List<Region>
) {
    val selectedRegionData = regionMap[value]
    val selectedRegionString =
        if (selectedRegionData != null) stringResource(selectedRegionData.label) else ""

    Box(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        AppExposedDropdownMenu(
            value = selectedRegionString,
            label = { Text(stringResource(R.string.region)) },
            leadingIcon = { RegionIcon(selectedRegionData?.drawable) },
            modifier = Modifier.fillMaxWidth(),
            items = { close ->
                options.forEach { item ->
                    val regionData = regionMap.getValue(item)
                    DropdownMenuItem(
                        onClick = {
                            setValue(item)
                            close()
                        }
                    ) {
                        ListItem(
                            icon = { Image(painterResource(regionData.drawable), null) },
                            text = { Text(stringResource(regionData.label)) }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun RegionIcon(@DrawableRes resIcon: Int?) {
    if (resIcon != null)
        Image(painterResource(resIcon), null)
    else
        Icon(Icons.Outlined.Public, null)
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ScreenPreview() {
    val regions = listOf(Region.EU, Region.US)

    AppTheme {
        LoginScreen("", {}, null, null, {}, regions, { _, _ -> })
    }
}