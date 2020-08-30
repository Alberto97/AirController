package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.models.BottomSheetListItem
import org.alberto97.hisenseair.ui.BottomSheetListItem
import org.alberto97.hisenseair.ui.theme.AppTheme

abstract class BottomSheetListDialog<T : Enum<*>> : BottomSheetDialogFragment() {

    abstract val title: String

    abstract fun onItemClick(data: T)

    abstract fun getList(): List<BottomSheetListItem<T>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val list = getList()

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
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
            }
        }

    }

    override fun dismiss() {
        lifecycleScope.launch {
            delay(500L)
            super.dismiss()
        }
    }
}