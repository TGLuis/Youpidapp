package lufra.youpidapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

object IntentHelper {
    /**
     * Create a widget intent and update the {code views} with the pending intent and the viewID
     * @param context: the Activity context
     * @param cls: the class used to create the Intent with the context
     * @param appWidgetId: the widget id to be added to the intent
     * @param isService: a boolean, true if the pending intent must start a service, or an activity
     * @param flag: an additional flag to start the service/activity
     * @param views: the RemoteViews which will contain the pendingIntent
     * @param viewId: the viewId which will be contained by {code views}
     */
    fun createWidgetIntent(context: Context, cls: Class<*>, appWidgetId: Int, isService: Boolean,
                           flag: Int, views: RemoteViews, viewId: Int) {
        val intent = Intent(context, cls)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntent = if (isService) PendingIntent.getService(context, appWidgetId, intent, flag) else PendingIntent.getActivity(context, appWidgetId, intent, flag)
        views.setOnClickPendingIntent(viewId, pendingIntent)
    }
}