package org.alberto97.hisenseair.bottomsheet

import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class ComposeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun dismiss() {
        lifecycleScope.launch {
            delay(500L)
            super.dismiss()
        }
    }
}