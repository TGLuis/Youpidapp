package fragments

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.Sound
import adapter.SoundAdapter
import android.animation.Animator
import android.util.Log
import android.widget.Button
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class MainFragment: MyFragment() {
    private lateinit var context: MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SoundAdapter
    private lateinit var viewLayoutManager: RecyclerView.LayoutManager
    override var TAG: String = "=====MAINFRAGMENT====="

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // Not sure if the following should be put in onCreateView or in onActivityCreated
        viewLayoutManager = LinearLayoutManager(view.context)
        viewAdapter = SoundAdapter(Sound.ALL_SOUNDS_STR.mapIndexed { index, s ->
            Sound(index, s)
        }, object: SoundAdapter.Listener {
            override fun onSoundClicked(soundHolder: SoundAdapter.SoundHolder) {
                soundHolder.setIsRecyclable(false)
                val button = soundHolder.button
                val soundName = soundHolder.sound!!.name
                val duration = context.discotheque.play(soundName)
                val animator = AnimatorInflater.loadAnimator(context, R.animator.fade)
                animator.setTarget(button)
                animator.duration = duration.toLong()
                animator.addListener(object: Animator.AnimatorListener {
                    val TAG = "MainFragment::onCreateView::AnimatorListener"
                    override fun onAnimationEnd(animation: Animator?) {
                        Log.i(TAG, "animation for $soundHolder ${soundHolder.button} end $animation")
                    }
                    override fun onAnimationStart(animation: Animator?) {
                        Log.i(TAG, "animation for $soundHolder ${soundHolder.button} start $animation")
                    }
                    override fun onAnimationCancel(animation: Animator?) {
                        Log.i(TAG, "animation for $soundHolder cancel $animation")
                    }
                    override fun onAnimationRepeat(animation: Animator?) {
                        Log.i(TAG, "animation for $soundHolder repeat $animation")
                    }
                })
                animator.start()
                Log.i(TAG, "Clicked, $animator")
            }
        })
        recyclerView = view.findViewById<RecyclerView>(R.id.sound_recyclerview).apply {
            layoutManager = viewLayoutManager
            adapter = viewAdapter
        }

        // TODO need to add onCreateOptionsMenu handler + the SearchView, and onQueryTextChange + onQueryTextSubmit handlers

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val randomButton = context.findViewById<Button>(R.id.button_random)
        randomButton.setOnClickListener { button ->
            val duration = context.discotheque.playRandom()
            addAnimator(button, duration.toLong())
        }
        context.setMenu("home")
    }

    private fun addAnimator(view: View, duration: Long) {
        val animator = AnimatorInflater.loadAnimator(context, R.animator.fade)
        animator.setTarget(view)
        animator.duration = duration.toLong()
        animator.start()
        Log.i(TAG, "$view, $animator")
    }
}
