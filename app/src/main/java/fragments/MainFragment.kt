package fragments;

import android.animation.AnimatorInflater
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
        iterateOverChildrenAndSetButtons(parent)
        context.setMenu("home")
    }

    private fun iterateOverChildrenAndSetButtons(parent: LinearLayout) {
        parent.children.forEach { it ->
            if (it::class == LinearLayout::class) {
                iterateOverChildrenAndSetButtons(it as LinearLayout)
            } else {
                val idName = resources.getResourceEntryName(it.id)
                if (idName == "random"){
                    it.setOnClickListener {button ->
                        val duration = context.discotheque.playRandom()
                        addAnimator(button, duration.toLong())
                    }
                } else {
                    it.setOnClickListener {button ->
                        val duration = context.discotheque.play(idName)
                        addAnimator(button, duration.toLong())
                    }
                }
            }
        }
    }

    private fun addAnimator(view: View, duration: Long) {
        val animator = AnimatorInflater.loadAnimator(context, R.animator.fade)
        animator.setTarget(view)
        animator.duration = duration.toLong()
        animator.start()
    }
}
