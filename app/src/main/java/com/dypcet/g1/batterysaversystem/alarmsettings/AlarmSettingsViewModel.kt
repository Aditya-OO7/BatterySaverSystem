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
import com.dypcet.g1.batterysaversystem.utils.SERVICE_ALARM
import com.dypcet.g1.batterysaversystem.utils.PERCENTAGE_EXTRA
import com.dypcet.g1.batterysaversystem.utils.SERVICE_TYPE
import com.dypcet.g1.batterysaversystem.utils.StateType

class AlarmSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = AlarmSettingsViewModel::class.java.simpleName

    private var _percentage = MutableLiveData<Float>()
    val percentage: LiveData<Float> = _percentage

    private var _alarmState = MutableLiveData<StateType>()
    val alarmState: LiveData<StateType> = _alarmState

    private val _isChargingOn = MutableLiveData<Boolean>()
    val isChargingOn: LiveData<Boolean> = _isChargingOn

    private val sharedPref =
        SharedPreferenceManager.getInstance(getApplication<Application>().applicationContext)

    val startButtonVisible = Transformations.map(_alarmState) {
        it == StateType.OFF
    }

    val stopButtonVisible = Transformations.map(_alarmState) {
        it == StateType.ON
    }

    init {
        _isChargingOn.value = getChargeState()

        _percentage.value = sharedPref.getAlarmPercentage()

        _alarmState.value = sharedPref.getAlarmState()
    }

    fun saveAlarmStateAndPercentage() {
        sharedPref.putAlarmPercentage(_percentage.value ?: 0F)
        sharedPref.putAlarmState(_alarmState.value ?: StateType.OFF)
    }

    fun setPercentage(value: Float) {
        _percentage.value = value
    }

    fun onSetAlarm() {
        Log.d(TAG, "onSetAlarm called")

        // Check if device is in charging state
        onRefresh()
        if (!_isChargingOn.value!!) {
            return
        }

        // Check if current battery level is greater than that of battery level set for alarm
        val currentPercentage = getCurrentPercentage()
        if (_percentage.value!! < currentPercentage) {
            Toast.makeText(
                getApplication(),
                "Percentage set less than minimum !",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // All clear! Start the alarm service
        Intent(
            getApplication<Application>().applicationContext,
            ChargeAlarmService::class.java
        ).also { i ->
            i.putExtra(PERCENTAGE_EXTRA, _percentage.value)
            i.putExtra(SERVICE_TYPE, SERVICE_ALARM)
            getApplication<Application>().applicationContext?.startService(i)
        }

        // Notify Alarm is on
        _alarmState.value = StateType.ON
    }

    fun onStopAlarm() {
        Log.d(TAG, "onStopAlarm called")

        // Stop ringtone if charging is complete and alarm ringtone is on
        AlarmService.getInstance(getApplication<Application>().applicationContext).stopAlarmIfOn()

        // Now comes the end! Stop the alarm service
        Intent(
            getApplication<Application>().applicationContext,
            ChargeAlarmService::class.java
        ).also { i ->
            getApplication<Application>().applicationContext.stopService(i)
        }

        // notify Alarm is off
        _alarmState.value = StateType.OFF
    }

    fun onRefresh() {
        _isChargingOn.value = getChargeState()
    }

    private fun getChargeState(): Boolean {
        val batteryStatus: Intent = getBatteryChangedIntent()
        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        return (status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL)
    }

    private fun getCurrentPercentage(): Float {
        val batteryStatus: Intent = getBatteryChangedIntent()

        return batteryStatus.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
    }

    private fun getBatteryChangedIntent(): Intent {
        return IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
            getApplication<Application>().registerReceiver(null, intentFilter)
        }!!
    }
}