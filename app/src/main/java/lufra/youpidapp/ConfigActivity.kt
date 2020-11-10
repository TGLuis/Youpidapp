package lufra.youpidapp

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*

class ConfigActivity : Activity() {
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    //var selectedType: Int = 0
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

        val button = findViewById<Button>(R.id.create_widget)
        button.setOnClickListener {
            showAppWidget()
        }

    }

    private fun showAppWidget() {
        val intentOne = Intent(this, PlayRandomWidget::class.java)
        intentOne.putExtra("sound_to_play", selectedSong)
        intentOne.action = ACTION_SIMPLEAPPWIDGET
        val pendingIntentOne = PendingIntent.getActivity(this, appWidgetId, intentOne, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntentOne)

        views.setTextViewText(R.id.name_sound, this.getText(resources.getIdentifier(selectedSong, "string", packageName)))

        widgetManager.updateAppWidget(appWidgetId, views)


        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}