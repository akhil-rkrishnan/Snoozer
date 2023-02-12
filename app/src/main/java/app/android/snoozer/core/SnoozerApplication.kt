package app.android.snoozer.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import app.android.snoozer.utils.GENERAL_NOTIFICATION_CHANNEL_ID
import app.android.snoozer.utils.GENERAL_NOTIFICATION_CHANNEL_NAME

class SnoozerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createGeneralNotificationChannel()
    }

    private fun createGeneralNotificationChannel() {
        val channel = NotificationChannel(
            GENERAL_NOTIFICATION_CHANNEL_ID,
            GENERAL_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "This channel sends general notifications"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}