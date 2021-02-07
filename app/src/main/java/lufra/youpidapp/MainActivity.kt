package lufra.youpidapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragments.*
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private val TAG = "==== MAINACTIVITY ===="

    lateinit var actionBarButtons: ActionBarButtons
    lateinit var frags: Stack<MyFragment>
    private lateinit var lesFragments: MutableMap<String, MyFragment>
    lateinit var toolbar: Toolbar
    lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private var searchMenuItem: MenuItem? = null
    lateinit var discotheque: Discotheque

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        actionBarButtons = ActionBarButtons(this)
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
        var type = Helper.getConfigValue("reading_type")
        var pitch = Helper.getConfigValue("pitch")
        while (type == null || pitch == null) {
            Helper.restart(this)
            Thread.sleep(50)
            type = Helper.getConfigValue("reading_type")
            pitch = Helper.getConfigValue("pitch")
            Log.e("MainActivity", "restart reading type")
        }
        discotheque.setType(type.toInt())
        discotheque.setPitch(pitch.toFloat())


        //NavigationView
        navView = this.findViewById(R.id.nav_view)
        setDrawer()

        // Fragments
        frags = Stack()
        lesFragments = HashMap()
        openFragment(MainFragment::class.java.name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun setDrawer() {
        val context = this
        navView.menu.clear()
        navView.menu.add(R.string.home).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(MainFragment::class.java.name)
                true
            }
        }
        navView.menu.add(R.string.boite_title).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(BoiteFragment::class.java.name)
                true
            }
        }
        navView.menu.add(R.string.parameters).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(ParameterFragment::class.java.name)
                true
            }
        }
        navView.menu.add(R.string.about_title).apply {
            setOnMenuItemClickListener {
                drawerLayout.closeDrawers()
                context.openFragment(AboutFragment::class.java.name)
                true
            }
        }
    }

    fun openFragment(fragmentName: String, pop: Boolean = false) {
        if (!lesFragments.containsKey(fragmentName)) {
            lesFragments[fragmentName] = Class.forName(fragmentName).newInstance() as MyFragment
        }
        val frag = lesFragments[fragmentName]!!
        if (!pop && (frags.empty() || frag::class != this.frags.peek()::class))
            frags.push(frag)
        supportFragmentManager.beginTransaction().replace(R.id.frame, frag, frag.TAG).commit()
    }

    /***********************************************************************************************
     * override of the activity functions
     */
    override fun onBackPressed() {
        when {
            frags.peek().isSearchOpened() -> frags.peek().closeSearchIfOpened()
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            frags.size >= 2 -> {
                frags.pop()
                openFragment(frags.peek()::class.java.name, true)
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
        Helper.setConfigValue("pitch", discotheque.getPitch().toString())
    }
}