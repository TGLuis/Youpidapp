package fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class MainFragment: MyFragment() {
    private lateinit var context: MainActivity
    override var TAG: String = "===== MAINFRAGMENT ====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val parent = context.findViewById<LinearLayout>(R.id.list_of_buttons)
        parent.children.forEach {
            val idName = resources.getResourceEntryName(it.id)
            if (idName != "random"){
                it.setOnClickListener {context.discotheque.play(idName)}
            } else {
                it.setOnClickListener {context.discotheque.playRandom()}
            }
        }
        context.setMenu("home", true)
    }
}
