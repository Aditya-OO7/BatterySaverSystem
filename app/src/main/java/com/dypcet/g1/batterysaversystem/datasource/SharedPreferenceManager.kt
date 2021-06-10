package com.dypcet.g1.batterysaversystem.datasource

import android.content.Context
import android.content.SharedPreferences
import com.dypcet.g1.batterysaversystem.R

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

    fun putPercentage(percentage: Float) {
        with(sharedPref.edit()) {
            putFloat(
                applicationContext.getString(R.string.percentage_key),
                percentage
            )
            apply()
        }
    }

    fun getPercentage(): Float {
        return sharedPref.getFloat(
            applicationContext.getString(R.string.percentage_key),
            0.0F
        )
    }

    fun putAlarmState(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(
                applicationContext.getString(R.string.alarm_set_key),
                state
            )
            apply()
        }
    }

    fun getAlarmState(): Boolean {
        return sharedPref.getBoolean(
            applicationContext.getString(R.string.alarm_set_key),
            true
        )
    }

    fun putChargeState(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(
                applicationContext.getString(R.string.charge_complete_key),
                state
            )
            apply()
        }
    }

    fun getChargeState(): Boolean {
        return sharedPref.getBoolean(
            applicationContext.getString(R.string.charge_complete_key),
            false
        )
    }
}