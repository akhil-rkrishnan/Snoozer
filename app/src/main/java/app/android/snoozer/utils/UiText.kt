package app.android.snoozer.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val message: String): UiText()
    data class FromStringResource(
        @StringRes val id: Int
    ): UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> message
            is FromStringResource -> stringResource(id = id)
        }
    }
}