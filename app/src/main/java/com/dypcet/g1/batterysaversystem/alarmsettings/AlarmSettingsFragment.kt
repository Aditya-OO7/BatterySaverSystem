package com.dypcet.g1.batterysaversystem.alarmsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAlarmSettingsBinding
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferenceManager
import com.dypcet.g1.batterysaversystem.utils.StateType
import com.google.android.material.slider.Slider

class AlarmSettingsFragment : Fragment() {

    private val TAG = AlarmSettingsFragment::class.java.simpleName

    private lateinit var viewBinding: FragmentAlarmSettingsBinding

    private lateinit var viewModel: AlarmSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings, container, false)
        viewBinding.lifecycleOwner = this

        viewModel = AlarmSettingsViewModel(requireActivity().application)

        viewBinding.viewmodel = viewModel

        viewBinding.slider.setLabelFormatter { value: Float -> "${value.toInt()}%" }
        viewBinding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            viewModel.setPercentage(value)
        })

        viewModel.alarmState.observe(viewLifecycleOwner, {
            if (it == StateType.ON) {
                viewBinding.moveSliderTextView.visibility = View.GONE
                viewBinding.batteryPercentage.visibility = View.GONE
                viewBinding.slider.visibility = View.GONE
                viewBinding.alarmSetTextView.visibility = View.VISIBLE
            } else {
                viewBinding.moveSliderTextView.visibility = View.VISIBLE
                viewBinding.batteryPercentage.visibility = View.VISIBLE
                viewBinding.slider.visibility = View.VISIBLE
                viewBinding.alarmSetTextView.visibility = View.GONE
            }
        })

        return viewBinding.root
    }

    override fun onDestroy() {
        viewModel.saveAlarmStateAndPercentage()
        super.onDestroy()
    }
}