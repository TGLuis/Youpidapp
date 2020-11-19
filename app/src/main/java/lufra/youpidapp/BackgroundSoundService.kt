package lufra.youpidapp

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BackgroundSoundService: Service() {
    private val discotheque = Discotheque(this)
    override fun onBind(intent: Intent?): IBinder? {
        Log.d("BACKGROUND", "onBind")
        return null
    }

    override fun onStart(intent: Intent, startId: Int) {
        Log.d("BACKGROUND", "onStart")
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
        val sound = ConfigActivity.loadSound(this, appWidgetId)
        if (sound == null)
            discotheque.playRandom()
        else
            discotheque.play(sound)
    }

}