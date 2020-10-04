package lufra.youpidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fragments.*
import java.util.*
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val TAG = "==== MAINACTIVITY ===="

    var screenOn: Boolean = false
    var volumeOn: Boolean = true

    private lateinit var frags: Stack<MyFragment>
    private lateinit var toolbar: Toolbar
    //private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private var lastMenu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        toolbar = this.findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        //drawerLayout = this.findViewById(R.id.drawer_layout)
        //val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //drawerLayout.addDrawerListener(toggle)
        //toggle.syncState()

        //NavigationView
        //navView = this.findViewById(R.id.nav_view)
        //setDrawer()

        // Fragments
        frags = Stack()
        openFragment(MainFragment())

        toolbar.setTitle(R.string.app_name)
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }*/

    /**
     * Contextual menu, create dynamically the menu in function of the parameter 'which'
     * If 'which' is "home" then it will create the menu for the home.
     * If 'which' is "project" it will create the menu for every screen with a project.
     */
    /*fun setMenu(which: String, force: Boolean = false) {
        val context = this
        val myMenu = toolbar.menu
        if (!force && lastMenu != null && lastMenu == which)
            return
        if (force)
            myMenu.clear()
        lastMenu = which
        when (which) {
            "home" -> {
                myMenu.clear()
            }
            "project" -> {
                myMenu.add("test").apply {
                    setOnMenuItemClickListener {
                        true
                    }
                }
            }
        }
    }*/

    /*private fun setDrawer() {
        val context = this
        navView.menu.clear()
        navView.menu.add("test").apply {
            setOnMenuItemClickListener {
                true
            }
        }
    }*/

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

    /***********************************************************************************************
     * Some more functions
     */
    private fun saveState() {
        // nothing
        return
    }
}