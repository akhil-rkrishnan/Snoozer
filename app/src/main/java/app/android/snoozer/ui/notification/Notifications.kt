package app.android.snoozer.ui.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import app.android.snoozer.R
import app.android.snoozer.data.helpers.receivers.NotificationReceiver
import app.android.snoozer.data.model.AlarmItem
import app.android.snoozer.ui.MainActivity
import app.android.snoozer.utils.ALARM_ITEM
import app.android.snoozer.utils.GENERAL_NOTIFICATION_CHANNEL_ID
import app.android.snoozer.utils.SERVICE_ENABLED
import app.android.snoozer.utils.toJsonString

class SnoozerNotification(private val context: Context) {

    fun getGeneralNotificationBuilder(alarmItem: AlarmItem?) = run {
        NotificationCompat.Builder(
            context, GENERAL_NOTIFICATION_CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.ic_android_robo)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(context.getString(R.string.snoozer_service_active))
            setContentIntent(PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ))
            getActions(alarmItem).forEach {
                addAction(it)
            }
        }.build()
    }


    fun getActions(alarmItem: AlarmItem?): List<NotificationCompat.Action> {
        return arrayListOf<NotificationCompat.Action>().apply {
            /*add(
                NotificationCompat.Action.Builder(
                    R.drawable.ic_start_service,
                    "Start service",
                    PendingIntent.getBroadcast(
                        context,
                        101,
                        Intent(context, NotificationReceiver::class.java).apply {
                            putExtra(SERVICE_ENABLED, true)
                            putExtra(ALARM_ITEM, alarmItem?.toJsonString())
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                ).build()
            )*/
            add(
                NotificationCompat.Action.Builder(
                    R.drawable.ic_stop_service,
                    "Stop service",
                    PendingIntent.getBroadcast(
                        context,
                        101,
                        Intent(context, NotificationReceiver::class.java).apply {
                            putExtra(SERVICE_ENABLED, false)
                            putExtra(ALARM_ITEM, alarmItem?.toJsonString())
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                ).build()
            )
        }
    }

}