package lufra.youpidapp

import adapter.SoundAdapter
import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.*
import data.Sound

class ConfigActivity : Activity() {
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    var selectedSong: String = "random"
    var selectedDisplay: String = "Random"
    private lateinit var widgetManager: AppWidgetManager
    private lateinit var views: RemoteViews
    private lateinit var sounds: MutableList<Sound>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_config)
        setResult(RESULT_CANCELED)

        widgetManager = AppWidgetManager.getInstance(this)
        views = RemoteViews(this.packageName, R.layout.sample_widget)
        // find widget id from intent
        if (intent.extras != null)
            appWidgetId = intent.extras!!.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        Helper.init(this)

        sounds = Helper.getSounds().sortedWith(SoundAdapter.mComparator).toMutableList()
        sounds.add(0, Sound("random", "Random"))

        makeSpinnerSound()

        val button = findViewById<Button>(R.id.create_widget)
        button.setOnClickListener { showAppWidget() }
    }

    private fun makeSpinnerSound() {
        val setupWidget = findViewById<Spinner>(R.id.spinner_sounds)
        val soundName = sounds.map { tuple -> tuple.name }
        val soundDisplay = sounds.map { tuple -> tuple.displayText }
        val adaptor = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, soundDisplay)
        adaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        setupWidget.adapter = adaptor
        val theSound = loadSound(this, appWidgetId)
        if (theSound == null) {
            setupWidget.setSelection(0)
        } else {
            val toSelect: Int = soundName.indexOf(theSound[0])
            if (toSelect == -1)
                setupWidget.setSelection(0)
            else
                setupWidget.setSelection(toSelect)
        }
        setupWidget.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedSong = sounds[p2].name
                    selectedDisplay = sounds[p2].displayText
                }
            }
    }

    private fun showAppWidget() {
        saveSound(this, appWidgetId, selectedSong, selectedDisplay)

        val views = RemoteViews(packageName, R.layout.sample_widget)

        // Construct an Intent which is pointing this class.
        val intentOne = Intent(this, BackgroundSoundService::class.java)
        intentOne.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentOne = PendingIntent.getService(this, appWidgetId, intentOne, 0)
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntentOne)

        val intentTwo = Intent(this, ConfigActivity::class.java)
        intentTwo.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val pendingIntentTwo = PendingIntent.getActivity(
            this, appWidgetId, intentTwo, PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.name_sound, pendingIntentTwo)

        views.setTextViewText(R.id.name_sound, selectedDisplay)

        widgetManager.partiallyUpdateAppWidget(appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun saveSound(context: Context, appWidgetId: Int, soundId: String, soundDisplay: String) {
        context.getSharedPreferences(PREFS_NAME, 0).edit()
            .putString(appWidgetId.toString(), "$soundId:::$soundDisplay")
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "lufra.youpidapp.ConfigActivity"
        fun loadSound(context: Context, appWidgetId: Int): List<String>? {
            val soundIdAndDisplay = context.getSharedPreferences(PREFS_NAME, 0)
                .getString(appWidgetId.toString(), "random:::Random") ?: return null
            return soundIdAndDisplay.split(":::")
        }
    }
}