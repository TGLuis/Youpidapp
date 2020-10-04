package fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import lufra.youpidapp.Discotheque
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
        val discotheque = Discotheque(context)

        val parent = context.findViewById<ConstraintLayout>(R.id.whole_screen)
        parent.children.forEach {
            val idName = resources.getResourceEntryName(it.id)
            if (idName != "random"){
                it.setOnClickListener {discotheque.play(idName)}
            } else {
                it.setOnClickListener {discotheque.play_random()}
            }
        }
        context.setMenu("home", true)
    }
}
