package app.android.snoozer.data.model

import androidx.core.text.isDigitsOnly
import java.time.LocalDateTime

data class AlarmItem(
    val localDateTime: LocalDateTime = LocalDateTime.now(),
    val interval: String = "",
    val intervalInMillis: Long = 60000L,
    val message: String? = null,
    val id : Int = 0
)

fun AlarmItem.triggerAlarm(block: () -> Unit): Unit? {
    return if (interval.isDigitsOnly()) {
        block()
    } else {
        null
    }
}