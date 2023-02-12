package app.android.snoozer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.snoozer.R
import app.android.snoozer.data.helpers.PreferenceService
import app.android.snoozer.data.helpers.alarm.AlarmScheduler
import app.android.snoozer.data.helpers.extensions.formatInMillisAs
import app.android.snoozer.data.model.AlarmItem
import app.android.snoozer.data.model.triggerAlarm
import app.android.snoozer.utils.TIME_INPUT
import app.android.snoozer.utils.TimeUnits
import app.android.snoozer.utils.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private lateinit var preferenceService: PreferenceService
    private lateinit var timeInput: StateFlow<String>
    private val _uiTextChannel = Channel<UiText>()
    val uiTextFlow get() = _uiTextChannel.receiveAsFlow()
    private var _alarmState by mutableStateOf(AlarmItem())
    val alarmState get() = _alarmState
    private lateinit var listener: ServiceFunctions
    private var _snoozerRunningStatus by mutableStateOf(false)
    val snoozerRunningStatus get() = _snoozerRunningStatus

    fun initialize(
        preferenceService: PreferenceService,
        listener: ServiceFunctions
    ) {
        this.preferenceService = preferenceService
        val initialValue = preferenceService.lastTimeInput().toString()
        timeInput = savedStateHandle.getStateFlow(TIME_INPUT, initialValue)
        _alarmState = _alarmState.copy(
            interval = initialValue.let { if (it == "0") "" else it },
            intervalInMillis = initialValue.formatInMillisAs(TimeUnits.SECONDS) ?: 60000L
        )
        this.listener = listener

    }

    fun updateTimeInput(value: String) {
        if (value.isBlank() || value.isEmpty()) {
            _alarmState = _alarmState.copy(
                interval = "",
                intervalInMillis = 60000L
            )
            preferenceService.saveTimeInput("0")
            return
        }
        if (value.isDigitsOnly()) {
            savedStateHandle[TIME_INPUT] = value
            _alarmState = _alarmState.copy(
                interval = value,
                intervalInMillis = value.formatInMillisAs(TimeUnits.SECONDS) ?: 60000L,
                id = if (_alarmState.interval != value) _alarmState.id + 1 else _alarmState.id
            )
            preferenceService.saveTimeInput(value)
        } else {
            sendToast(UiText.FromStringResource(R.string.invalid_time))
        }
    }

    fun scheduleAlarm() {
        _alarmState.triggerAlarm {
            listener.startService(_alarmState)
            sendToast(UiText.FromStringResource(R.string.alarm_scheduled))
            _snoozerRunningStatus = true
        } ?: sendToast(UiText.FromStringResource(R.string.input_valid_time))
    }


    fun cancelAlarm() {
        _snoozerRunningStatus = false
        sendToast(UiText.FromStringResource(R.string.alarm_cancelled))
        listener.cancelService(_alarmState)
    }

    fun updateSnoozerRunningStatus(value: Boolean) {
        _snoozerRunningStatus = value
    }


    private fun sendToast(uiText: UiText) {
        viewModelScope.launch {
            _uiTextChannel.send(uiText)
        }
    }
}