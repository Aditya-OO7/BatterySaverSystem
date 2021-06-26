package com.dypcet.g1.batterysaversystem.alertsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAlertSettingsBinding
import com.dypcet.g1.batterysaversystem.utils.StateType
import com.google.android.material.slider.Slider

class AlertSettingsFragment : Fragment() {

    private lateinit var viewBinding: FragmentAlertSettingsBinding

    private lateinit var viewModel: AlertSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_alert_settings,
                container,
                false
            )
        viewBinding.lifecycleOwner = this

        viewModel = AlertSettingsViewModel(requireActivity().application)

        viewBinding.viewmodel = viewModel

        viewBinding.slider.setLabelFormatter { value: Float -> "${value.toInt()}%" }
        viewBinding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            viewModel.setPercentage(value)
        })

        viewModel.alertState.observe(viewLifecycleOwner, {
            if (it == StateType.ON) {
                viewBinding.moveSliderTextView.visibility = View.GONE
                viewBinding.batteryPercentage.visibility = View.GONE
                viewBinding.slider.visibility = View.GONE
                viewBinding.alertSetTextView.visibility = View.VISIBLE
            } else {
                viewBinding.moveSliderTextView.visibility = View.VISIBLE
                viewBinding.batteryPercentage.visibility = View.VISIBLE
                viewBinding.slider.visibility = View.VISIBLE
                viewBinding.alertSetTextView.visibility = View.GONE
            }
        })

        return viewBinding.root
    }

    override fun onDestroy() {
        viewModel.saveAlertStateAndPercentage()
        super.onDestroy()
    }
}