package tgl.templateapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import library.*
import fragments.*
import java.util.*
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdRequest

class MainActivity : AppCompatActivity() {
    private val TAG = "==== MAINACTIVITY ===="

    var screenOn: Boolean = false
    var volumeOn: Boolean = true

    private lateinit var frags: Stack<MyFragment>
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var db: MyDatabase
    private lateinit var mAdView: AdView
    private var lastMenu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Database
        db = MyDatabase(this)

        // properties
        Helper.init(this)
        screenOn = Helper.getConfigValue("screen_on") == true.toString()
        volumeOn = Helper.getConfigValue("volume_on") == true.toString()
        if (screenOn) {
            // maintain screen open during activity
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        // Toolbar
        toolbar = this.findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = this.findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //NavigationView
        navView = this.findViewById(R.id.nav_view)
        setDrawer()

        // Fragments
        frags = Stack()
        openFragment(MyFragment())

        // pub
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
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
    }

    private fun setDrawer() {
        val context = this
        navView.menu.clear()
        navView.menu.add("test").apply {
            setOnMenuItemClickListener {
                true
            }
        }
    }

    fun openFragment(frag: MyFragment, pop: Boolean = false) {
        if (!pop && (frags.empty() || frag::class != this.frags.peek()::class))
            frags.push(frag)
        supportFragmentManager.beginTransaction().replace(R.id.frame, frag, frag.TAG()).commit()
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