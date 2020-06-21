package org.alberto97.hisenseair.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.CompatExtensions.setTint
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.bottomsheet.DeviceFanSpeedSheet
import org.alberto97.hisenseair.bottomsheet.DeviceWorkModeSheet
import org.alberto97.hisenseair.bottomsheet.TempSheet
import org.alberto97.hisenseair.databinding.FragmentDeviceBinding
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

object DeviceActivityRequest {
    const val WORK_MODE = 10
    const val FAN_SPEED = 11
    const val TEMP = 12
}

const val DEVICE_FRAGMENT_MODE_SHEET = "WorkModeSheet"
const val DEVICE_FRAGMENT_FAN_SHEET = "FanSpeedSheet"
const val DEVICE_FRAGMENT_TEMP_SHEET = "TempSheet"

class DeviceFragment : Fragment() {
    lateinit var binding: FragmentDeviceBinding

    private val args by navArgs<DeviceFragmentArgs>()
    private val viewModel: DeviceViewModel by viewModel  { parametersOf(args.dsn) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeviceBinding.inflate(layoutInflater)

        binding.power.hide()
        setSpinnerVisible(true)

        // Device name
        viewModel.deviceName.observe(viewLifecycleOwner, Observer {
            binding.toolbar.title = it
        })

        // Ambient temp
        viewModel.roomTemp.observe(viewLifecycleOwner, Observer {
            binding.tempSection.ambientTemp.text = getString(R.string.device_ambient_temp, it)
            binding.offLayout.roomTemp.text = getString(R.string.device_room_temp, it)
        })

        viewModel.temp.observe(viewLifecycleOwner, Observer {
            binding.tempSection.temp.text = getString(R.string.temp, it)
        })

        binding.tempSection.tempDown.setOnClickListener {
            viewModel.reduceTemp()
        }

        binding.tempSection.tempUp.setOnClickListener {
            viewModel.increaseTemp()
        }

        binding.tempSection.temp.setOnClickListener {
            val tempType = if (viewModel.useCelsius)
                TempType.Celsius
            else
                TempType.Fahrenheit

            val bundle = Bundle()
            bundle.putInt("current", viewModel.temp.value!!)
            bundle.putInt("tempType", tempType.value)

            val dialog = TempSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.TEMP)
            dialog.showNow(parentFragmentManager, DEVICE_FRAGMENT_TEMP_SHEET)
        }

        // Work mode
        viewModel.workState.observe(viewLifecycleOwner, Observer {
            val resId = modeToStringMap[it] ?: R.string.work_mode_unknown
            val drawableId = modeToIconMap[it] ?: R.drawable.round_brightness_7_24

            binding.workMode.text = getString(resId)
            binding.workMode.icon = drawableId
            binding.workMode.loading = false

            binding.offLayout.offMode.setImageResource(drawableId)
        })

        binding.workMode.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("current", viewModel.workState.value!!.value)

            val dialog = DeviceWorkModeSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.WORK_MODE)
            dialog.showNow(parentFragmentManager, DEVICE_FRAGMENT_MODE_SHEET)
        }

        // Air flow
        viewModel.fanSpeed.observe(viewLifecycleOwner, Observer {
            val resId = fanToStringMap[it] ?: R.string.fan_speed_unknown
            binding.fanSpeed.text = getString(resId)

            binding.fanSpeed.loading = false
        })

        binding.fanSpeed.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("current", viewModel.fanSpeed.value!!.value)

            val dialog = DeviceFanSpeedSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.FAN_SPEED)
            dialog.showNow(parentFragmentManager, DEVICE_FRAGMENT_FAN_SHEET)
        }

        viewModel.horizontalAirFlow.observe(viewLifecycleOwner, Observer {
            binding.horizontal.active = it
            binding.horizontal.loading = false
        })
        viewModel.verticalAirFlow.observe(viewLifecycleOwner, Observer {
            binding.vertical.active = it
            binding.vertical.loading = false
        })

        binding.horizontal.setOnClickListener {
            binding.horizontal.loading = true
            viewModel.switchAirFlowHorizontal()
        }
        binding.vertical.setOnClickListener {
            binding.vertical.loading = true
            viewModel.switchAirFlowVertical()
        }

        // Backlight
        viewModel.backlight.observe(viewLifecycleOwner, Observer {
            binding.backlight.active = it
            binding.backlight.loading = false
        })

        viewModel.isEco.observe(viewLifecycleOwner, Observer {
            binding.eco.active = it
            binding.eco.loading = false
        })

        viewModel.isQuiet.observe(viewLifecycleOwner, Observer {
            binding.quiet.active = it
            binding.quiet.loading = false
        })

        viewModel.isBoost.observe(viewLifecycleOwner, Observer {
            binding.boost.active = it
            binding.boost.loading = false
        })

        binding.backlight.setOnClickListener {
            binding.backlight.loading = true
            viewModel.switchBacklight()
        }
        binding.eco.setOnClickListener {
            binding.eco.loading = true
            viewModel.switchEco()
        }
        binding.quiet.setOnClickListener {
            binding.quiet.loading = true
            viewModel.switchQuiet()
        }
        binding.boost.setOnClickListener {
            binding.boost.loading = true
            viewModel.switchBoost()
        }

        binding.power.setOnClickListener {
            setSpinnerVisible(true)
            viewModel.switchPower()
        }

        viewModel.isOn.observe(viewLifecycleOwner, Observer {
            setSpinnerVisible(false)

            updateFabColor(it)
            updateLayoutVisibility(it)

            binding.power.show()
        })

        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setOnMenuItemClickListener { handleMenuClick(it) }

        return binding.root
    }

    private fun setSpinnerVisible(visible: Boolean) {
        binding.spinner.spinner.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun updateLayoutVisibility(isOn: Boolean) {
        binding.offLayout.offLayout.visibility = if (isOn) View.GONE else View.VISIBLE
        binding.list.visibility = if (isOn) View.VISIBLE else View.GONE
    }

    private fun updateFabColor(isOn: Boolean) {
        val res = if (isOn) R.color.primary else R.color.materialGray600
        binding.power.imageTintList = ColorStateList.valueOf(getColor(requireContext(), res))
    }

    private fun onFanSpeedChange(fanSpeed: FanSpeed) {
        binding.fanSpeed.loading = true
        viewModel.setFanSpeed(fanSpeed)
    }

    private fun onWorkModeChange(workMode: WorkMode) {
        binding.workMode.loading = true
        viewModel.setMode(workMode)
    }

    private fun onTempChange(temp: Int) {
        viewModel.setTemp(temp)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            DeviceActivityRequest.FAN_SPEED -> {
                val fanSpeed = data!!.getSerializableExtra(DEVICE_FRAGMENT_FAN_SHEET) as FanSpeed
                onFanSpeedChange(fanSpeed)
            }
            DeviceActivityRequest.WORK_MODE -> {
                val workMode = data!!.getSerializableExtra(DEVICE_FRAGMENT_MODE_SHEET) as WorkMode
                onWorkModeChange(workMode)
            }
            DeviceActivityRequest.TEMP -> {
                val temp = data!!.getIntExtra(DEVICE_FRAGMENT_TEMP_SHEET, 0)
                if (temp > 0)
                    onTempChange(temp)
            }
        }
    }

    private fun handleMenuClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val direct = DeviceFragmentDirections.actionDeviceFragmentNewToDevicePreferenceFragment(args.dsn)
                findNavController().navigate(direct)
                true
            }
            else -> false
        }
    }
}