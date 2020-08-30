package org.alberto97.hisenseair.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val appDarkColors = darkColors(
    primary = brandGreen
)

private val appLightColors = lightColors(
    primary = brandGreen
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) appDarkColors else appLightColors,
        content = content
    )
}