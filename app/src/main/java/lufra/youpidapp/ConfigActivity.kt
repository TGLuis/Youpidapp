package lufra.youpidapp

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*

class ConfigActivity : Activity() {
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    var selectedSong: String = "youpidou"
    private lateinit var widgetManager: AppWidgetManager
    private lateinit var views: RemoteViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("CONFIG", "onCreate")

        setContentView(R.layout.activity_config)
        setResult(RESULT_CANCELED)

        widgetManager = AppWidgetManager.getInstance(this)
        views = RemoteViews(this.packageName, R.layout.sample_widget)
        // find widget id from intent
        val extras = intent.extras
        if (extras != null)
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        makeSpinner()

        val button = findViewById<Button>(R.id.create_widget)
        button.setOnClickListener {
            showAppWidget()
        }

    }

    private fun makeSpinner() {
        val setupWidget = findViewById<Spinner>(R.id.spinner_sounds)
        val discotheque = Discotheque(this)
        val sounds = discotheque.getAllSound().toMutableList()
        //sounds.add(0, listOf<String>("random", "random"))
        val soundName = sounds.map { tuple -> tuple[0] }
        val adaptor = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, soundName)
        adaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        setupWidget.adapter = adaptor
        setupWidget.setSelection(0)
        setupWidget.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedSong = resources.getResourceName(resources.getIdentifier(sounds[p2][1], "raw", packageName))
                    selectedSong = selectedSong.split("/")[1]
                    Log.e("SELECTED", selectedSong)
                }
            }
    }

    private fun showAppWidget() {
        saveSound(this, appWidgetId, selectedSong)
        // We need to broadcast an APPWIDGET_UPDATE to our appWidget so it will update the user name TextView.
        /*val appWidgetManager = AppWidgetManager.getInstance(this)
        val updateIntent = Intent(this, PlayRandomWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this.packageName, PlayRandomWidget::class.simpleName!!))
        updateIntent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        this.sendBroadcast(updateIntent);


        // Done with Configure, finish Activity.
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish();*/

        val views = RemoteViews(packageName, R.layout.sample_widget)

        // Construct an Intent which is pointing this class.
        val intentOne = Intent(this, BackgroundSoundService::class.java)
        intentOne.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentOne = PendingIntent.getService(this, appWidgetId, intentOne, 0)
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntentOne)

        val intentTwo = Intent(this, ConfigActivity::class.java)
        intentTwo.action = ACTION_CHANGESOUNDWIDGET
        intentTwo.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentTwo = PendingIntent.getActivity(this, appWidgetId, intentTwo, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.name_sound, pendingIntentTwo)

        val identifierSound = resources.getIdentifier(selectedSong, "string", packageName)
        views.setTextViewText(R.id.name_sound, getText(identifierSound))

        widgetManager.partiallyUpdateAppWidget(appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun saveSound(context: Context, appWidgetId: Int, sound: String) {
        context.getSharedPreferences(PREFS_NAME, 0).edit()
            .putString(appWidgetId.toString(), sound)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "lufra.youpidapp.ConfigActivity"
        fun loadSound(context: Context, appWidgetId: Int): String? {
            return context.getSharedPreferences(PREFS_NAME, 0).getString(appWidgetId.toString(), "random")
        }
    }
}