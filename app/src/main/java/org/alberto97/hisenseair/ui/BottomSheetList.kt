package org.alberto97.hisenseair.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.models.BottomSheetListItem

@Composable
fun <T: Enum<*>> BottomSheetList(
    title: String,
    list: List<BottomSheetListItem<T>>,
    onItemClick: (value: T) -> Unit
) {
    Surface {
        Column {
            Text(
                title,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumnFor(list) {
                BottomSheetListItem(
                    id = it.id,
                    text = it.name,
                    icon = it.icon,
                    selected = it.current,
                    onClick = { data -> onItemClick(data) }
                )
            }
        }
    }
}