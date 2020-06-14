package org.alberto97.hisenseair.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.hisenseair.CompatExtensions.setTint
import org.alberto97.hisenseair.databinding.ListItemWorkModeBinding
import org.alberto97.hisenseair.models.WorkModeItem

class WorkModeAdapter(val callback: (WorkModeItem) -> Unit) : ListAdapter<WorkModeItem, WorkModeAdapter.WorkModeViewHolder>(
    WorkModeDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorkModeViewHolder(
            ListItemWorkModeBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun onBindViewHolder(holder: WorkModeViewHolder, position: Int) = holder.bind(getItem(position))

    inner class WorkModeViewHolder(private val binding: ListItemWorkModeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workMode: WorkModeItem){

            binding.modeText.text = workMode.name
            binding.image.setImageResource(workMode.icon)
            binding.image.setTint(workMode.tint)
            binding.setImage.visibility = if (workMode.current) View.VISIBLE else View.INVISIBLE

            binding.root.setOnClickListener { callback(workMode) }
        }
    }
}

private class WorkModeDiffCallback : DiffUtil.ItemCallback<WorkModeItem>() {
    override fun areItemsTheSame(oldItem: WorkModeItem, newItem: WorkModeItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorkModeItem, newItem: WorkModeItem): Boolean {
        return oldItem.id == newItem.id
    }
}