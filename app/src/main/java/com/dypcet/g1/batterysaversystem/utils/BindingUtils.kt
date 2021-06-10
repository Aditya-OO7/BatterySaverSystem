package com.dypcet.g1.batterysaversystem.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dypcet.g1.batterysaversystem.R

// Add Binding Adapters here
@BindingAdapter("setBatteryPercentageText")
fun TextView.setTextForSettingBatteryPercentage(percentage: Float?) {
    text = this.context.getString(R.string.battery_percentage_string, percentage?.toInt())
}

@BindingAdapter("alarmSetDoneText")
fun TextView.setTextForAlarmSettingDone(percentage: Float?) {
    text = this.context.getString(R.string.alarm_set_done, percentage?.toInt())
}