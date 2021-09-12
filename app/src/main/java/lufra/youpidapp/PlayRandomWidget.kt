package lufra.youpidapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent
import android.util.Log


class PlayRandomWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.sample_widget)

        // Construct an Intent which is pointing this class.
        IntentHelper.createWidgetIntent(context, BackgroundSoundService::class.java, appWidgetId,
            true, 0, views, R.id.widget_button)
        IntentHelper.createWidgetIntent(context,ConfigActivity::class.java, appWidgetId,
            false, PendingIntent.FLAG_UPDATE_CURRENT, views, R.id.name_sound)

        val soundIdAndDisplay = ConfigActivity.loadSound(context, appWidgetId)
        Log.e("PLAY WIDGET", soundIdAndDisplay.toString())
        views.setTextViewText(R.id.name_sound, soundIdAndDisplay?.get(1) ?:"Random")

        // Instruct the widget manager to update the widget
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
    }
}

