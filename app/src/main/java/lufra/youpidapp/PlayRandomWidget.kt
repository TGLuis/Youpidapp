package lufra.youpidapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle

const val ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE"
const val ACTION_CHANGESOUNDWIDGET = "ACTION_CHANGESOUNDWIDGET"
const val WIDGET_TAG = "SAMPLE_WIDGET_TAG"

class PlayRandomWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle? ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        if (context != null && appWidgetManager != null)
            updateAppWidget(context, appWidgetManager, appWidgetId)
    }


    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.sample_widget)

        // Construct an Intent which is pointing this class.
        val intentOne = Intent(context, BackgroundSoundService::class.java)
        intentOne.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentOne = PendingIntent.getService(context, appWidgetId, intentOne, 0)
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntentOne)

        val intentTwo = Intent(context, ConfigActivity::class.java)
        intentTwo.action = ACTION_CHANGESOUNDWIDGET
        intentTwo.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentTwo = PendingIntent.getActivity(context, appWidgetId, intentTwo, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.name_sound, pendingIntentTwo)

        val soundToPlay = ConfigActivity.loadSound(context, appWidgetId)
        val identifierSound = context.resources.getIdentifier(soundToPlay, "string", context.packageName)
        views.setTextViewText(R.id.name_sound, context.getText(identifierSound))

        // Instruct the widget manager to update the widget
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
    }
}

