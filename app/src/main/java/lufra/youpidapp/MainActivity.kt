package lufra.youpidapp

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragments.*
import java.util.Stack
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private val TAG = "==== MAINACTIVITY ===="

    lateinit var actionBarButtons: ActionBarButtons
    lateinit var frags: Stack<MyFragment>
    private lateinit var lesFragments: MutableMap<String, MyFragment>
    lateinit var toolbar: Toolbar
    lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
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
        DataPersistenceHelper.init(this)
        discotheque = Discotheque(this)
        discotheque.setType(DataPersistenceHelper.getPreferredPlayType())
        discotheque.setPitch(DataPersistenceHelper.getPreferredPitch())

        //NavigationView
        navView = this.findViewById(R.id.nav_view)
        setDrawer()

        // Fragments
        frags = Stack()
        lesFragments = HashMap()
        if (DataPersistenceHelper.shouldOpenOnFavorites())
            openFragment(FavoritesFragment::class.java.name)
        else
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
        navView.menu.add(Menu.NONE, 0, Menu.NONE, R.string.home).apply {
            setOnMenuItemClickListener {
                context.openFragment(MainFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
                true
            }
        }
        navView.menu.add(R.string.favorites).apply {
            setOnMenuItemClickListener {
                context.openFragment(FavoritesFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
                true
            }
        }
        navView.menu.add(R.string.boite_title).apply {
            setOnMenuItemClickListener {
                context.openFragment(BoiteFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
                true
            }
        }
        navView.menu.add(Menu.NONE, 3, Menu.NONE, R.string.parameters).apply {
            setOnMenuItemClickListener {
                context.openFragment(ParameterFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
                true
            }
        }
        navView.menu.add(R.string.contact_title).apply{
            setOnMenuItemClickListener {
                context.openFragment(ContactFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
                true
            }
        }
        navView.menu.add(R.string.about_title).apply {
            setOnMenuItemClickListener {
                context.openFragment(AboutFragment::class.java.name)
                drawerLayout.closeDrawer(GravityCompat.START, false)
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
                drawerLayout.closeDrawer(GravityCompat.START, false)
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
        DataPersistenceHelper.setPreferredPitch(discotheque.getPitch())
        DataPersistenceHelper.setPreferredPlayType(discotheque.getPlayType())
    }
}