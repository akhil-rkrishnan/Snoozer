package app.android.snoozer.data.helpers.extensions

import androidx.core.text.isDigitsOnly
import app.android.snoozer.utils.TimeUnits

fun String.isValidTime(onValid: (String) -> Unit): Unit? {
    return if (this.isDigitsOnly()) {
        onValid(this)
    } else {
        null
    }
}

fun String.formatInMillisAs(unit: TimeUnits): Long? {
    if (this.isEmpty() || !this.isDigitsOnly())
        return null
    return when (unit) {
        TimeUnits.SECONDS -> {
            this.toLong() * 60000
        }
        TimeUnits.MINUTES -> {
            (this.toLong() / 60000)
        }
        else -> null
    }
}
