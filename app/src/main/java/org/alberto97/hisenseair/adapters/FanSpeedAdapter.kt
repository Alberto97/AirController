package org.alberto97.hisenseair.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.hisenseair.databinding.ListItemFanSpeedBinding
import org.alberto97.hisenseair.models.FanSpeedItem

class FanSpeedAdapter(val callback: (FanSpeedItem) -> Unit) : ListAdapter<FanSpeedItem, FanSpeedAdapter.ViewHolder>(
    FanSpeedDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemFanSpeedBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ListItemFanSpeedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FanSpeedItem){
            binding.name.text = item.name
            binding.setImage.visibility =  if (item.current) View.VISIBLE else View.INVISIBLE

            binding.root.setOnClickListener { callback(item) }
        }
    }
}

private class FanSpeedDiffCallback : DiffUtil.ItemCallback<FanSpeedItem>() {
    override fun areItemsTheSame(oldItem: FanSpeedItem, newItem: FanSpeedItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FanSpeedItem, newItem: FanSpeedItem): Boolean {
        return oldItem.id == newItem.id
    }
}