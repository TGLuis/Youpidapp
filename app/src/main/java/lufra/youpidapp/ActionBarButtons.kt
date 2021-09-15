package lufra.youpidapp

import android.graphics.ColorFilter
import android.view.Menu
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fragments.ParameterFragment

class ActionBarButtons(activity: MainActivity) {

    private var context: MainActivity = activity

    fun addResearch(menu: Menu): MenuItem {
        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_search_24)
        val col = ContextCompat.getColor(context, R.color.black)
        iconDrawable?.setTint(col)
        return menu.add(Menu.NONE, 0, Menu.NONE, R.string.search).apply {
            icon = iconDrawable
            actionView = SearchView(context)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
            (actionView as SearchView).maxWidth = Integer.MAX_VALUE
        }
    }

    fun addStop(menu: Menu): MenuItem {
        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_stop_24)
        val col = ContextCompat.getColor(context, R.color.black)
        iconDrawable?.setTint(col)
        return menu.add(Menu.NONE, 1, Menu.NONE, R.string.stop).apply {
            icon = iconDrawable
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setOnMenuItemClickListener {
                context.discotheque.stopAll()
                context.frags.peek().stopAll()
                true
            }
//            actionView.tag = "btn_stop"
        }

    }

    fun addParameters(menu: Menu): MenuItem {
        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_settings_24)
        val col = ContextCompat.getColor(context, R.color.black)
        iconDrawable?.setTint(col)
        return menu.add(Menu.NONE, 2, Menu.NONE, R.string.parameters).apply {
            icon = iconDrawable
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            setOnMenuItemClickListener {
                context.openFragment(ParameterFragment::class.java.name)
                true
            }
        }
    }

    fun addTypeLecture(menu: Menu): MenuItem {
        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_playlist_play_24)
        val col = ContextCompat.getColor(context, R.color.black)
        iconDrawable?.setTint(col)
        return menu.add(R.string.type).apply {
            icon = iconDrawable
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            setOnMenuItemClickListener {
                dialogAndSetType()
                true
            }
        }
    }

    fun dialogAndSetType() {
        val viewInflated = LayoutInflater.from(context)
                .inflate(R.layout.simple_spinner_input, context.navView as ViewGroup, false)
        val theSpinner = viewInflated.findViewById<Spinner>(R.id.type_spinner)
        val adaptor = ArrayAdapter(
                context,
                R.layout.support_simple_spinner_dropdown_item,
                context.discotheque.getTypes()
        )
        var selectedType = PlayType.SINGLE
        adaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        theSpinner.adapter = adaptor
        theSpinner.setSelection(context.discotheque.getPlayType().ordinal)
        theSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        selectedType = PlayType.fromInt(p2)
                    }
                }

        MaterialAlertDialogBuilder(context, R.style.AlertDialogPositiveBtnFilled)
                .setView(viewInflated)
                .setTitle(R.string.type)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    context.discotheque.setType(selectedType)
                    Toast.makeText(
                            context,
                            "Mode de lecture: " + context.discotheque.getTypes()[context.discotheque.getPlayType().ordinal],
                            Toast.LENGTH_LONG
                    ).show()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
    }
}