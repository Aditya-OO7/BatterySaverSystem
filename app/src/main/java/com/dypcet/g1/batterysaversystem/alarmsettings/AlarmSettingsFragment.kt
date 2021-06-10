package com.dypcet.g1.batterysaversystem.alarmsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAlarmSettingsBinding
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferenceManager
import com.google.android.material.slider.Slider

class AlarmSettingsFragment : Fragment() {

    private lateinit var viewBinding: FragmentAlarmSettingsBinding

    private lateinit var viewModel: AlarmSettingsViewModel

    private lateinit var sharedPref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings, container, false)
        viewBinding.lifecycleOwner = this

        viewModel = AlarmSettingsViewModel(this.requireActivity().application)

        viewBinding.viewmodel = viewModel

        sharedPref = SharedPreferenceManager.getInstance(context?.applicationContext!!)

        viewBinding.slider.setLabelFormatter { value: Float -> "${value.toInt()}%" }
        viewBinding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            viewModel.setPercentage(value)
//            viewBinding.batteryPercentage.text =
//                getString(R.string.battery_percentage_string, value.toInt())
        })

        return viewBinding.root
    }

    override fun onDestroy() {
        sharedPref.putPercentage(viewModel.percentage.value!!)
        sharedPref.putAlarmState(viewModel.alarmSet.value!!)
        sharedPref.putChargeState(AlarmSettingsViewModel.chargeComplete.value!!)
        super.onDestroy()
    }
}