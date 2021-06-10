package com.dypcet.g1.batterysaversystem.alarmsettings

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferenceManager
import com.dypcet.g1.batterysaversystem.services.AlarmService
import com.dypcet.g1.batterysaversystem.services.ChargeAlarmService

const val PERCENTAGE_EXTRA = "PERCENTAGE"

class AlarmSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = AlarmSettingsViewModel::class.java.simpleName

    private var _percentage = MutableLiveData<Float>()
    val percentage: LiveData<Float> = _percentage

    private var _alarmSet = MutableLiveData<Boolean>()
    val alarmSet: LiveData<Boolean> = _alarmSet

    private val _isChargingOn = MutableLiveData<Boolean>()
    val isChargingOn: LiveData<Boolean> = _isChargingOn

    private val sharedPref =
        SharedPreferenceManager.getInstance(getApplication<Application>().applicationContext)

    val startButtonVisible = Transformations.map(_alarmSet) {
        !it
    }

    val stopButtonVisible = Transformations.map(_alarmSet) {
        it
    }

    init {

        _isChargingOn.value = getChargeState()

        _percentage.value = sharedPref.getPercentage()

        _alarmSet.value = sharedPref.getAlarmState()

        chargeComplete.value = sharedPref.getChargeState()

    }

    companion object {
        val chargeComplete = MutableLiveData(false)
    }

    fun setPercentage(value: Float) {
        _percentage.value = value
    }

    fun onSetAlarm() {
        Log.d(TAG, "onSetAlarm called")
        chargeComplete.value = false
        onRefresh()
        if (!_isChargingOn.value!!) {
            return
        }
        val currentPercentage = getCurrentPercentage()
        if (_percentage.value!! < currentPercentage) {
            Toast.makeText(
                getApplication(),
                "Percentage set less than minimum !",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        Intent(
            getApplication<Application>().applicationContext,
            ChargeAlarmService::class.java
        ).also { i ->
            i.putExtra(PERCENTAGE_EXTRA, _percentage.value)
            getApplication<Application>().applicationContext?.startService(i)
        }

        _alarmSet.value = true

        Log.d(TAG, "onSetAlarm finished")
    }

    fun onStopAlarm() {
        Log.d(TAG, "onStopAlarm called")
        onRefresh()
        if (!_isChargingOn.value!!) {
            return
        }
        if (chargeComplete.value!!) {
            AlarmService.getInstance(getApplication<Application>().applicationContext).stopAlarm()
        }
        Intent(
            getApplication<Application>().applicationContext,
            ChargeAlarmService::class.java
        ).also { i ->
            getApplication<Application>().applicationContext.stopService(i)
        }

        _alarmSet.value = false
        Log.d(TAG, "onStopAlarm finished")
    }

    fun onRefresh() {
        _isChargingOn.value = getChargeState()
    }

    private fun getChargeState(): Boolean {
        val batteryStatus: Intent? =
            IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
                getApplication<Application>().registerReceiver(null, intentFilter)
            }
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

        return (status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL)
    }

    private fun getCurrentPercentage(): Float {
        val batteryStatus: Intent? =
            IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
                getApplication<Application>().registerReceiver(null, intentFilter)
            }

        val batteryPct = batteryStatus?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        return batteryPct!!
    }

}