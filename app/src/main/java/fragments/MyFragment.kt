package fragments

import androidx.fragment.app.Fragment

open class MyFragment : Fragment() {
    open fun TAG(): String {
        return "== MYFRAGMENT =="
    }
}