package org.alberto97.hisenseair.ui

//import androidx.compose.foundation.Text
//import androidx.compose.foundation.layout.padding
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.alberto97.hisenseair.models.BottomSheetListItem

@ExperimentalMaterialApi
@Composable
fun <T: Enum<*>> BottomSheetList(
    title: String,
    list: List<BottomSheetListItem<T>>,
    onItemClick: (value: T) -> Unit
) {
    /*Text(
        title,
        modifier = Modifier.padding(16.dp)
    )*/
    LazyColumn {
        items(list) {
            BottomSheetListItem(
                id = it.id,
                text = stringResource(it.name),
                icon = it.icon,
                selected = it.current,
                onClick = { data -> onItemClick(data) }
            )
        }
    }
}