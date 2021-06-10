package com.dypcet.g1.batterysaversystem.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dypcet.g1.batterysaversystem.MainActivity
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.alarmsettings.AlarmSettingsViewModel
import com.dypcet.g1.batterysaversystem.alarmsettings.PERCENTAGE_EXTRA
import com.dypcet.g1.batterysaversystem.receivers.PowerConnectionReceiver

class ChargeAlarmService : Service() {

    private val TAG = ChargeAlarmService::class.java.simpleName

    private val receiver = PowerConnectionReceiver()

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            receiver.setPercentage(msg.arg2.toFloat())

            IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
                applicationContext.registerReceiver(
                    receiver,
                    intentFilter
                )
                Log.d(TAG, "onStartCommand receiver registered")
            }
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand Called")
        val percentage = intent?.getFloatExtra(PERCENTAGE_EXTRA, 0.0F)!!
        Log.d(TAG, "onStartCommand Percentage : $percentage")

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            msg.arg2 = percentage.toInt()
            serviceHandler?.sendMessage(msg)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0)

        val notification = NotificationCompat.Builder(
            this,
            applicationContext.getString(R.string.notification_channel_id)
        )
            .setContentTitle("Charge Alarm Service")
            .setContentText("Charge Alarm set to $percentage")
            .setSmallIcon(R.drawable.egg_icon)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        Log.d(TAG, "onStartCommand Finished")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        if (!(AlarmSettingsViewModel.chargeComplete.value!!)) {
            applicationContext.unregisterReceiver(receiver)
            Log.d(TAG, "onDestroy receiver unregistered")
        }
        Log.d(TAG, "onDestroy Called")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}