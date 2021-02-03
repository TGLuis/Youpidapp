package fragments

import android.view.Menu
import androidx.fragment.app.Fragment

open class MyFragment : Fragment() {
    open var TAG: String = "===MyFragment==="
    open lateinit var menu: Menu
    open fun setMenu() { throw NotImplementedError("Menu Not implemented !")}
    open fun stopAll() {}
    open fun isSearchOpened(): Boolean = false
    open fun closeSearchIfOpened() {}
}