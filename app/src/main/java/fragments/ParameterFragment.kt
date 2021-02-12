package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import lufra.youpidapp.Discotheque
import lufra.youpidapp.MainActivity
import lufra.youpidapp.PlayType
import lufra.youpidapp.R

class ParameterFragment: MyFragment() {
    private lateinit var context: MainActivity
    private lateinit var typeSpinner: Spinner
    private lateinit var pitchSeekBar: SeekBar
    private lateinit var setDefaultButton: Button
    override var TAG: String = "=====BOITEFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_parameters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        typeSpinner = context.findViewById(R.id.type_spinner)
        typeSpinner.setSelection(context.discotheque.getType().ordinal)
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    context.discotheque.setType(PlayType.fromInt(p2))
                }
            }

        pitchSeekBar = context.findViewById(R.id.pitch_seekBar)
        pitchSeekBar.progress = ((context.discotheque.getPitch() - 0.5f) * 20f).toInt()
        pitchSeekBar.setOnSeekBarChangeListener(MyListener(context.discotheque))

        setDefaultButton = context.findViewById(R.id.default_settings_button)
        setDefaultButton.setOnClickListener {
            pitchSeekBar.progress =  ((context.discotheque.getDefaultPitch() - 0.5f) * 20f).toInt()
            typeSpinner.setSelection(0)
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

