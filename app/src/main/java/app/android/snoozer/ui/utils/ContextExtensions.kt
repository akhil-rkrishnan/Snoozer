package app.android.snoozer.ui.utils

import android.content.Context
import android.widget.Toast
import app.android.snoozer.utils.UiText

fun Context.showToast(uiText: UiText?) {
    if (uiText == null)
        return
    when (uiText) {
        is UiText.DynamicString -> {
            Toast.makeText(this, uiText.message, Toast.LENGTH_SHORT).show()
        }
        is UiText.FromStringResource -> {
            Toast.makeText(this, this.getString(uiText.id), Toast.LENGTH_SHORT).show()
        }
    }
}