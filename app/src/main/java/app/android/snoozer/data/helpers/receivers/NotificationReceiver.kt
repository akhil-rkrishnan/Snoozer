package app.android.snoozer.data.helpers.receivers

import android.content.*
import app.android.snoozer.data.service.SnoozerService
import app.android.snoozer.utils.ALARM_ITEM
import app.android.snoozer.utils.SERVICE_ENABLED
import app.android.snoozer.utils.asAlarmItem

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {

    private  var binder: SnoozerService.LocalBinder ?= null

    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceEnabled = intent?.getBooleanExtra(SERVICE_ENABLED, false) ?: return
        val alarmItem = intent.getStringExtra(ALARM_ITEM).asAlarmItem() ?: return
        // peeking running service, if is available then we get the binder object
        binder = peekService(
            context,
            Intent(context, SnoozerService::class.java),
        ) as SnoozerService.LocalBinder
        binder?.let {
            val isRunning = it.isSchedulerRunning()
            if (!serviceEnabled && isRunning) {
                it.cancelSnoozerService(alarmItem)
            }
        }
    }
}