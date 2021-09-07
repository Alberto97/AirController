package org.alberto97.hisenseair.ui.preferences

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val prefIconTint = Color(0xFF757575)

@Composable
fun PreferenceIcon(@DrawableRes id: Int) {
    PreferenceIcon(painterResource(id))
}

@Composable
fun PreferenceIcon(painter: Painter) {
    PrefIconBox {
        Icon(
            painter,
            contentDescription = null,
            tint = prefIconTint
        )
    }
}

@Composable
fun PreferenceIcon(imageVector: ImageVector) {
    PrefIconBox {
        Icon(
            imageVector,
            contentDescription = null,
            tint = prefIconTint
        )
    }
}

@Composable
private fun PrefIconBox(content: @Composable (() -> Unit)) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}