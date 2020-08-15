package org.alberto97.hisenseair.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.ListItemDeviceBinding
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.fragments.MainFragmentDirections
import org.alberto97.hisenseair.models.AppDevice

class DevicesAdapter() : ListAdapter<AppDevice, DevicesAdapter.DeviceViewHolder>(
    DevicesDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeviceViewHolder(
            ListItemDeviceBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) = holder.bind(getItem(position))

    inner class DeviceViewHolder(private val binding: ListItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: AppDevice) = with(binding) {

            name.text = device.name
            temp.text = device.temp
            power.isChecked = device.isPower

            val res = modeToIconMap[device.mode] ?: R.drawable.round_brightness_7_24
            mode.setImageResource(res)

            root.setOnClickListener { view ->
                val direction = MainFragmentDirections.actionMainFragmentToDeviceFragment(device.dsn)
                view.findNavController().navigate(direction)
            }
        }
    }
}

private class DevicesDiffCallback : DiffUtil.ItemCallback<AppDevice>() {

    override fun areItemsTheSame(
        oldItem: AppDevice,
        newItem: AppDevice
    ): Boolean {
        return oldItem.dsn == newItem.dsn
    }

    override fun areContentsTheSame(
        oldItem: AppDevice,
        newItem: AppDevice
    ): Boolean {
        return oldItem.isPower == newItem.isPower
    }
}