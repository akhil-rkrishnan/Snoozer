package app.android.snoozer.data.helpers.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import app.android.snoozer.data.helpers.receivers.AlarmReceiver
import app.android.snoozer.data.model.AlarmItem
import app.android.snoozer.data.repository.AlarmRepository
import app.android.snoozer.utils.BROADCAST_MESSAGE

private const val TAG = "AlarmScheduler"
class AlarmScheduler(
    private val context: Context
) : AlarmRepository {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    private var isRunning = false

    override fun scheduleAndRepeat(alarmItem: AlarmItem) {
        Log.e(TAG, "scheduleAndRepeat: scheduled")
        Log.e(TAG, "interval: ${alarmItem.intervalInMillis}", )
        Log.e(TAG, "current time: ${System.currentTimeMillis()}", )
        val pendingIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(BROADCAST_MESSAGE, alarmItem.message)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + alarmItem.intervalInMillis,
            alarmItem.intervalInMillis,
            PendingIntent.getBroadcast(
                context,
                alarmItem.id,
                pendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        isRunning = true
    }

    override fun schedule(alarmItem: AlarmItem) {

    }

    override fun cancel(alarmItem: AlarmItem) {
        Log.e(TAG, "Alarm cancelled!", )
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        isRunning = false
    }

    fun getSchedulerRunningStatus() = isRunning

}