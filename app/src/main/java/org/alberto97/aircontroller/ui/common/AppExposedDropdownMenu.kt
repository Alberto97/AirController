package org.alberto97.aircontroller.ui.common

import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import org.alberto97.aircontroller.ui.theme.AppTheme


@ExperimentalMaterialApi
@Composable
fun AppExposedDropdownMenu(
    value: String,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    items: @Composable (select: () -> Unit) -> Unit,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (textFieldSize, setTextFieldSize) = remember { mutableStateOf(Size.Zero) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { setExpanded(!expanded) }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    setTextFieldSize(coordinates.size.toSize())
                },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            modifier = Modifier.width(
                with(LocalDensity.current) { textFieldSize.width.toDp() }
            )
        ) {
            items { setExpanded(false) }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            AppExposedDropdownMenu("", items = {})
        }
    }
}