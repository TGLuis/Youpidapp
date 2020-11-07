package fragments

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            override fun onSoundClicked(soundViewHolder: SoundAdapter.SoundViewHolder) {
                viewAdapter.notifyItemChanged(soundViewHolder.adapterPosition, SoundAdapter.SoundItemAnimation.SOUND_CLIKED)
                /*val soundName = soundViewHolder.sound!!.name
                val duration = context.discotheque.play(soundName)
                val animator = AnimatorInflater.loadAnimator(context, R.animator.fade)*/
                //val soundHolderAnimation = SoundHolderAnimation(animator)
                //soundViewHolder.addAnimation(soundHolderAnimation)  // That way, the animation will be paused/resumed
                /*animator.setTarget(soundViewHolder.button)
                animator.duration = duration.toLong()*/
                Log.i(TAG, "Clicked")
            }
        })
        fun f(soundViewHolder: SoundAdapter.SoundViewHolder): Animator {
            val soundName = soundViewHolder.sound!!.name
            val duration = context.discotheque.play(soundName)
            val animator: Animator = AnimatorInflater.loadAnimator(context, R.animator.fade)
            animator.duration = duration.toLong()
            return animator
        }
        val itemAnim = SoundAdapter.SoundItemAnimation(::f)
        recyclerView = view.findViewById<RecyclerView>(R.id.sound_recyclerview).apply {
            layoutManager = viewLayoutManager
            adapter = viewAdapter
            itemAnimator = itemAnim
        }
        Log.i(TAG, "RecyclerView ItemAnimator: ${recyclerView.itemAnimator}")

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

    /*class SoundHolderAnimation(val animator: Animator): SoundAdapter.Animation {
        val TAG = "MainFragment::SoundHolderAnimation"
        init {
            //
        }

        override fun pause(soundViewHolder: SoundAdapter.SoundViewHolder) {
            animator.pause()  // If it fails, it may be because it is not running on the same thread ¯\_(ツ)_/¯
            animator.setTarget(null)
            Log.i(TAG, "anim pause, $animator")
        }

        override fun resume(soundViewHolder: SoundAdapter.SoundViewHolder) {
            val button = soundViewHolder.button
            animator.setTarget(button)
            animator.resume()
            Log.i(TAG, "anim resumed, $animator")
        }
    }*/
}
