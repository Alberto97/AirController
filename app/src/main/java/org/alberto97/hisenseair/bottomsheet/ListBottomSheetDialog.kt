package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.hisenseair.databinding.DialogListBinding
import org.alberto97.hisenseair.models.ListItemBase

abstract class ListBottomSheetDialog<T: ListItemBase<*>, V: Enum<*>> : BottomSheetDialogFragment() {

    abstract val title: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DialogListBinding.inflate(inflater, container, false)

        val list = getList()

        // Set current value
        val value = getCurrentValue()
        if (value != null) {
            list.forEach { i -> i.current = i.id == value }
        }

        val adapter = getAdapter()
        binding.recyclerView.adapter = adapter
        binding.sheetTitle.text = title

        adapter.submitList(list)

        return binding.root
    }

    abstract fun getCurrentValue(): V?

    abstract fun getList(): ArrayList<T>

    abstract fun getAdapter(): ListAdapter<T, *>
}