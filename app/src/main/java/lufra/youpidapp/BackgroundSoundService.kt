package lufra.youpidapp

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder

class BackgroundSoundService: Service() {
    companion object {
        var discotheque: Discotheque? = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent, startId: Int) {
        if (discotheque == null) discotheque = Discotheque(this)
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
        val sound = ConfigActivity.loadSound(this, appWidgetId)
        if (sound == null || sound == "random")
            discotheque!!.playRandom()
        else
            discotheque!!.play(sound)
    }

}