package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import lufra.youpidapp.*

class ParameterFragment: MyFragment() {
    private lateinit var context: MainActivity
    private lateinit var typeSpinner: Spinner
    private lateinit var pitchSeekBar: SeekBar
    private lateinit var setDefaultButton: Button
    private lateinit var openOnFavoritesCheckBox: CheckBox
    private lateinit var themeSpinner: Spinner
    override var TAG: String = "=====BOITEFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_parameters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        typeSpinner = context.findViewById(R.id.type_spinner)
        typeSpinner.setSelection(context.discotheque.getPlayType().ordinal)
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (context.discotheque.getPlayType().ordinal != p2)
                        context.discotheque.setType(PlayType.fromInt(p2))
                }
            }

        openOnFavoritesCheckBox = context.findViewById(R.id.checkbox_open_on_favorites)
        openOnFavoritesCheckBox.isChecked = DataPersistenceHelper.shouldOpenOnFavorites()
        openOnFavoritesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            DataPersistenceHelper.openOnFavorites(isChecked)
        }

        pitchSeekBar = context.findViewById(R.id.pitch_seekBar)
        pitchSeekBar.progress = ((context.discotheque.getPitch() - 0.5f) * 20f).toInt()
        pitchSeekBar.setOnSeekBarChangeListener(MyListener(context.discotheque))

        setDefaultButton = context.findViewById(R.id.default_settings_button)
        setDefaultButton.setOnClickListener {
            pitchSeekBar.progress =  ((context.discotheque.getDefaultPitch() - 0.5f) * 20f).toInt()
        }

        themeSpinner = context.findViewById(R.id.theme_spinner)
        val nightModeActivated = DataPersistenceHelper.isNightModeActivated()
        val theme_spinner_position: Int
        if (nightModeActivated) {
            themeSpinner.setSelection(1)
            theme_spinner_position = 1
        } else {
            themeSpinner.setSelection(0)
            theme_spinner_position = 0
        }
        val nightMode = AppCompatDelegate.getDefaultNightMode()

        //themeSpinner.setSelection()
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != theme_spinner_position) {
                    if (p2 == 0) {  // DayMode
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        DataPersistenceHelper.deactivateNightMode()
                    } else {        // NightMode
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        DataPersistenceHelper.activateNightMode()
                    }
                    recreate(context);
                }
            }
        }

        context.setTitle(R.string.parameters)
    }

    override fun setMenu() {
        val menu = context.toolbar.menu
        menu.clear()
    }

    private class MyListener(val discotheque: Discotheque): SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            discotheque.setPitch((progress.toFloat() /20f) + 0.5f)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

}

