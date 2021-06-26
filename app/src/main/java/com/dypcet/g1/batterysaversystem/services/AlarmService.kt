package com.dypcet.g1.batterysaversystem.services

import android.content.Context
import android.media.RingtoneManager
import android.util.Log

class AlarmService(applicationContext: Context) {

    private val ringtone = RingtoneManager.getRingtone(
        applicationContext,
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    )

    private var _isAlarmOn = false

    companion object {

        @Volatile
        private var INSTANCE: AlarmService? = null

        fun getInstance(applicationContext: Context): AlarmService {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AlarmService(applicationContext)

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun startAlarm() {
        Log.d("Alarm Ringtone", "Ringtone Started")
        ringtone.play()
        _isAlarmOn = true
    }

    fun stopAlarmIfOn() {
        if (_isAlarmOn) {
        Log.d("Alarm Ringtone", "Ringtone Stopped")
            ringtone.stop()
            _isAlarmOn = false
        }
    }
}