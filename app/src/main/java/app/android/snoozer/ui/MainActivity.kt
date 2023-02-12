package app.android.snoozer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import app.android.snoozer.core.theme.SnoozerTheme
import app.android.snoozer.data.helpers.PreferenceService
import app.android.snoozer.data.model.AlarmItem
import app.android.snoozer.data.service.SnoozerService
import app.android.snoozer.ui.utils.showToast
import app.android.snoozer.utils.ALARM_ITEM
import app.android.snoozer.utils.ENABLE_SNOOZER
import app.android.snoozer.utils.toJsonString
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity(), ServiceFunctions {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mService: SnoozerService
    private lateinit var binder: SnoozerService.LocalBinder
    private var mBound: Boolean = false
    private var mSeviceStarted: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            binder = service as SnoozerService.LocalBinder
            mService = binder.getService()
            mSeviceStarted = binder.isSchedulerRunning()
            viewModel.updateSnoozerRunningStatus(mSeviceStarted)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        setContent {
            SnoozerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 25.dp),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.uiTextFlow.collectLatest {
                showToast(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, SnoozerService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    private fun initialize() {
        viewModel.initialize(
            preferenceService = PreferenceService(this),
            this
        )
    }

    override fun startService(alarmItem: AlarmItem?) {
        if (mBound && !mSeviceStarted) {
            mService.startService(Intent(this, SnoozerService::class.java).apply {
                putExtra(ALARM_ITEM, alarmItem?.toJsonString())
                putExtra(ENABLE_SNOOZER, true)
            })
            mSeviceStarted = true
        } else {
            binder.startSnoozerService(alarmItem)
        }
    }

    override fun cancelService(alarmItem: AlarmItem?) {
        if (mSeviceStarted) {
            binder.cancelSnoozerService(alarmItem)
        }
    }
}

interface ServiceFunctions {
    fun startService(alarmItem: AlarmItem?)
    fun cancelService(alarmItem: AlarmItem?)
}

