package lufra.youpidapp

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import fragments.AboutFragment
import fragments.BoiteFragment
import fragments.MainFragment
import fragments.MyFragment
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "==== MAINACTIVITY ===="

    private lateinit var frags: Stack<MyFragment>
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private var lastMenu: String? = null
    lateinit var discotheque: Discotheque

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // widget
        val intent = Intent(this, PlayRandomWidget::class.java)
        intent.action = "ACTIVITY_ACTION"
        AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, PlayRandomWidget::class.java))
        sendBroadcast(intent)

        // Toolbar
        toolbar = this.findViewById(R.id.my_toolbar)
        drawerLayout = this.findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setTitle(R.string.app_name)

        // Helper
        Helper.init(this)
        discotheque = Discotheque(this)
        discotheque.setType(Helper.getConfigValue("reading_type")!!.toInt())

        //NavigationView
        navView = this.findViewById(R.id.nav_view)
        setDrawer()

        // Fragments
        frags = Stack()
        openFragment(MainFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Contextual menu, create dynamically the menu in function of the parameter 'which'
     * If 'which' is "home" then it will create the menu for the home.
     * If 'which' is "project" it will create the menu for every screen with a project.
     */
    fun setMenu(which: String, force: Boolean = false) {
        val context = this
        val myMenu = toolbar.menu
        if (!force && lastMenu != null && lastMenu == which)
            return
        if (force)
            myMenu.clear()
        lastMenu = which
        when (which) {
            "home" -> {
                myMenu.add(R.string.stop).apply {
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_stop_24)
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                    setOnMenuItemClickListener {
                        discotheque.stopAll()
                        true
                    }
                }
                myMenu.add(R.string.type).apply {
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_playlist_play_24)
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    setOnMenuItemClickListener {
                        dialogAndSetType()
                        true
                    }
                }
            }
            "nothing" -> {
                myMenu.clear()
            }
        }
    }

    private fun dialogAndSetType() {
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.simple_spinner_input, this.navView as ViewGroup, false)
        val theSpinner = viewInflated.findViewById<Spinner>(R.id.input_spinner)
        val adaptor = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, discotheque.getTypes())
        var selectedType = 1
        adaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        theSpinner.adapter = adaptor
        theSpinner.setSelection(discotheque.getType()-1)
        theSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long ) {
                    selectedType = p2 + 1
                }
            }
        MaterialAlertDialogBuilder(this, R.style.AlertDialogPositiveBtnFilled)
            .setView(viewInflated)
            .setTitle(R.string.type)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                discotheque.setType(selectedType)
                discotheque.toastOfType()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun setDrawer() {
        val context = this
        navView.menu.clear()
        navView.menu.add(R.string.home).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(MainFragment())
                true
            }
        }
        navView.menu.add(R.string.boite_title).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(BoiteFragment())
                true
            }
        }
        navView.menu.add(R.string.about_title).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(AboutFragment())
                true
            }
        }
    }

    fun openFragment(frag: MyFragment, pop: Boolean = false) {
        if (!pop && (frags.empty() || frag::class != this.frags.peek()::class))
            frags.push(frag)
        supportFragmentManager.beginTransaction().replace(R.id.frame, frag, frag.TAG).commit()
    }

    /***********************************************************************************************
     * override of the activity functions
     */
    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            frags.size >= 2 -> {
                frags.pop()
                openFragment(frags.peek(), true)
            }
            else -> {
                saveState()
                super.onBackPressed()
            }
        }
    }


    override fun onDestroy() {
        saveState()
        super.onDestroy()
    }

    override fun onPause() {
        saveState()
        super.onPause()
    }

    override fun onStop() {
        saveState()
        super.onStop()
    }

    private fun saveState() {
        Helper.setConfigValue("reading_type", discotheque.getType().toString())
    }
}