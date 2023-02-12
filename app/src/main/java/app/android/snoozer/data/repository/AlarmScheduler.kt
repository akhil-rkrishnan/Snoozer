package app.android.snoozer.data.repository

import app.android.snoozer.data.model.AlarmItem

interface AlarmRepository {
    fun scheduleAndRepeat(alarmItem: AlarmItem)
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}