package app.android.snoozer.data.helpers.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import app.android.snoozer.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //val message = intent?.getStringExtra(BROADCAST_MESSAGE) ?: return
        Log.e("AlarmReceiver", "Triggered alarm per minute",)
        val mediaPlayer = MediaPlayer.create(context, R.raw.snoozer)
//        mediaPlayer.prepare()
        mediaPlayer.start()
    }

}