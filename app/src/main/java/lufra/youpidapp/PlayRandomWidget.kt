package lufra.youpidapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent

const val ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE"
const val ACTION_CHANGESOUNDWIDGET = "ACTION_CHANGESOUNDWIDGET"
const val WIDGET_TAG = "SAMPLE_WIDGET_TAG"

class PlayRandomWidget : AppWidgetProvider() {
    private var discotheque: Discotheque? = null
    private var soundToPlay: String? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        Log.e("WIDGET", "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        Log.d(WIDGET_TAG,"enabled")
    }

    override fun onDisabled(context: Context) {
        Log.d(WIDGET_TAG,"disabled")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.e("WIDGET", "onReceive")
        Log.e("WIDGET", intent.action?:"No action")

        if ((intent.action.equals(ACTION_SIMPLEAPPWIDGET))) {
            if (discotheque == null) discotheque = Discotheque(context)
            if (soundToPlay == null)
                discotheque!!.playRandom()
            else
                discotheque!!.play(soundToPlay!!)
        } else if ((intent.action.equals(ACTION_CHANGESOUNDWIDGET))) {
            Log.e("WIDGET", "appWidgetID = ${intent.getIntExtra("appWidgetId", 0)}")
        }
    }


    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                appWidgetId: Int) {

        Log.e("TAG", "id = $appWidgetId")

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.sample_widget)


        // Construct an Intent which is pointing this class.
        val intentOne = Intent(context, PlayRandomWidget::class.java)
        intentOne.action = ACTION_SIMPLEAPPWIDGET
        val pendingIntentOne = PendingIntent.getActivity(context, appWidgetId, intentOne, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntentOne)

        val intentTwo = Intent(context, PlayRandomWidget::class.java)
        intentTwo.action = ACTION_CHANGESOUNDWIDGET
        intentTwo.putExtra("appWidgetId", appWidgetId)
        val pendingIntentTwo = PendingIntent.getBroadcast(context, appWidgetId, intentOne, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.name_sound, pendingIntentTwo)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

