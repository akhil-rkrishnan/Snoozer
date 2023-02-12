package app.android.snoozer.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import app.android.snoozer.data.helpers.alarm.AlarmScheduler
import app.android.snoozer.data.model.AlarmItem
import app.android.snoozer.ui.notification.SnoozerNotification
import app.android.snoozer.utils.ENABLE_SNOOZER
import app.android.snoozer.utils.SERVICE_NOTIFICATION_ID
import app.android.snoozer.utils.asAlarmItem

private const val TAG = "SnoozerService"

class SnoozerService : Service() {

    private val localBinder = LocalBinder()
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var snoozerNotification: SnoozerNotification

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: created")
        alarmScheduler = AlarmScheduler(this)
        snoozerNotification = SnoozerNotification(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.e(TAG, "onBind: binded")
        return localBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: sticked")
        val snoozerStart = intent?.getBooleanExtra(ENABLE_SNOOZER, false)
        val alarmItem = intent?.getStringExtra("alarm_item")?.asAlarmItem()
        alarmItem?.let {
            if (snoozerStart == true) {
                startSnoozerService(it)
            } else {
                stopSnoozerService(it)
            }
        }
        return START_STICKY

    }

    fun startSnoozerService(alarmItem: AlarmItem?) {
        Log.e(TAG, "startSnoozerService: started in foreground")
        alarmItem?.let {
            alarmScheduler.scheduleAndRepeat(alarmItem)
            startForeground(
                SERVICE_NOTIFICATION_ID,
                snoozerNotification.getGeneralNotificationBuilder(alarmItem)
            )
        }
    }

    fun stopSnoozerService(alarmItem: AlarmItem?) {
        Log.e(TAG, "stopSnoozerService: stopped")
        alarmItem?.let {
            alarmScheduler.cancel(it)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun getAlarmSchedulerStatus(): Boolean = alarmScheduler.getSchedulerRunningStatus()

    inner class LocalBinder : Binder() {
        fun getService(): SnoozerService = this@SnoozerService

        fun startSnoozerService(alarmItem: AlarmItem?) {
            getService().startSnoozerService(alarmItem = alarmItem)
        }

        fun cancelSnoozerService(alarmItem: AlarmItem?) {
            getService().stopSnoozerService(alarmItem = alarmItem)
        }

        fun isSchedulerRunning() = getService().getAlarmSchedulerStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}