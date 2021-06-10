package com.dypcet.g1.batterysaversystem.services

import android.app.Application
import android.content.Context
import android.media.RingtoneManager

class AlarmService(applicationContext: Context) {

    private val ringtone = RingtoneManager.getRingtone(
        applicationContext,
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    )

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
        ringtone.play()
    }

    fun stopAlarm() {
        ringtone.stop()
    }
}