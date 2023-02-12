package app.android.snoozer.data.helpers

import android.content.Context
import app.android.snoozer.data.repository.PreferenceRepository
import app.android.snoozer.utils.KEY_TIME_INPUT
import app.android.snoozer.utils.PREFERENCE

class PreferenceService(private val context: Context) : PreferenceRepository {

    private val preference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    private val editor = preference.edit()

    override fun lastTimeInput(): Long {
        return preference.getInt(KEY_TIME_INPUT, 0).toLong()
    }

    override fun saveTimeInput(input: String) {
        editor.putInt(KEY_TIME_INPUT, input.toInt()).apply()
    }


}