package com.dypcet.g1.batterysaversystem.datasource

import android.content.Context
import android.content.SharedPreferences
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.utils.StateType

class SharedPreferenceManager(val applicationContext: Context) {

    private val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
        applicationContext.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var INSTANCE: SharedPreferenceManager? = null

        fun getInstance(applicationContext: Context): SharedPreferenceManager {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = SharedPreferenceManager(applicationContext)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

    fun putAlarmPercentage(percentage: Float) {
        with(sharedPref.edit()) {
            putFloat(
                applicationContext.getString(R.string.alarm_percentage_key),
                percentage
            )
            apply()
        }
    }

    fun getAlarmPercentage(): Float {
        return sharedPref.getFloat(
            applicationContext.getString(R.string.alarm_percentage_key),
            0.0F
        )
    }

    fun putAlarmState(state: StateType) {
        with(sharedPref.edit()) {
            putBoolean(
                applicationContext.getString(R.string.alarm_state_key),
                when (state) {
                    StateType.ON -> true
                    else -> false
                }
            )
            apply()
        }
    }

    fun getAlarmState(): StateType {
        return when (sharedPref.getBoolean(
            applicationContext.getString(R.string.alarm_state_key),
            false
        )) {
            true -> StateType.ON
            else -> StateType.OFF
        }
    }

    fun putAlertPercentage(percentage: Float) {
        with(sharedPref.edit()) {
            putFloat(
                applicationContext.getString(R.string.alert_percentage_key),
                percentage
            )
            apply()
        }
    }

    fun getAlertPercentage(): Float {
        return sharedPref.getFloat(
            applicationContext.getString(R.string.alert_percentage_key),
            0.0F
        )
    }

    fun putAlertState(state: StateType) {
        with(sharedPref.edit()) {
            putBoolean(
                applicationContext.getString(R.string.alert_state_key),
                when (state) {
                    StateType.ON -> true
                    else -> false
                }
            )
            apply()
        }
    }

    fun getAlertState(): StateType {
        return when (sharedPref.getBoolean(
            applicationContext.getString(R.string.alert_state_key),
            false
        )) {
            true -> StateType.ON
            else -> StateType.OFF
        }
    }
}