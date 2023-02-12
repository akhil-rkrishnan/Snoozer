package app.android.snoozer.utils

import app.android.snoozer.data.model.AlarmItem

fun String?.asAlarmItem(): AlarmItem? {
    return this?.fromJson(AlarmItem::class.java)
}