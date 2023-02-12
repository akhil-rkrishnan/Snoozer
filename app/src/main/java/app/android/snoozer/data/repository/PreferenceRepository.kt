package app.android.snoozer.data.repository

interface PreferenceRepository {
    fun lastTimeInput(): Long
    fun saveTimeInput(input: String)
}