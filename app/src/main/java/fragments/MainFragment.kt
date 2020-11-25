package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import data.Sound
import adapter.SoundAdapter
import android.animation.*
import android.content.res.Configuration
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import lufra.youpidapp.Helper
import lufra.youpidapp.MainActivity
import lufra.youpidapp.R

class MainFragment: MyFragment() {
    private lateinit var context: MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SoundAdapter
    private lateinit var viewLayoutManager: GridLayoutManager
    private var viewColumnCount = 1
    override var TAG: String = "=====MAINFRAGMENT====="
    private lateinit var animatorsPlaying: MutableList<Animator>

    private val soundClickedListener = object: SoundAdapter.SoundClickedListener {
        override fun onSoundClicked(soundViewHolder: SoundAdapter.SoundViewHolder) {
            val sound = soundViewHolder.sound!!
            val soundName = sound.name
            val duration = context.discotheque.play(soundName)
            playWtfAnimator(sound, duration.toLong())
            Log.d(TAG, "Clicked, $soundViewHolder")
        }
    }
    private val animCleanup = object: SoundAdapter.CleanupAnimationListener {
        override fun onUnbind(soundViewHolder: SoundAdapter.SoundViewHolder) {
            soundViewHolder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }
    private val soundFilterListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }
        override fun onQueryTextChange(newText: String?): Boolean {
            viewAdapter.filter(newText)
            recyclerView.scrollToPosition(0)
            return true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        context = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // Not sure if the following should be put in onCreateView or in onActivityCreated
        viewColumnCount = resources.getInteger(R.integer.grid_column_count)
        viewLayoutManager = GridLayoutManager(view.context, viewColumnCount)
        viewAdapter = SoundAdapter(Helper.getSounds(), soundClickedListener)
        recyclerView = view.findViewById<RecyclerView>(R.id.sound_recyclerview).apply {
            layoutManager = viewLayoutManager
            adapter = viewAdapter
        }
        animatorsPlaying = mutableListOf()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val randomButton = context.findViewById<Button>(R.id.button_random)
        randomButton.setOnClickListener { button ->
            val duration = context.discotheque.playRandom()
            playTargetedAnimator(button, duration.toLong())
        }
        context.setMenu("soundbox")
        // Mandatory to do this now...
        context.setFilterQueryTextListener(soundFilterListener)
    }

    override fun stopAll() {
        var i = animatorsPlaying.size-1
        while (i >= 0) {
            animatorsPlaying[i].cancel()
            i--
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "config changed: $newConfig")
        viewColumnCount = resources.getInteger(R.integer.grid_column_count)
        viewLayoutManager.spanCount = viewColumnCount
    }

    private fun playWtfAnimator(sound: Sound, duration: Long) {
        // Actually it is an ObjectAnimator, but we will not specify its target
        val animator = AnimatorInflater.loadAnimator(context, R.animator.fade) as ValueAnimator
        animator.addUpdateListener { va ->
            val animVal = va?.animatedValue as Int
            val viewHolder = viewAdapter.getActiveViewHolder(sound)
            viewHolder?.button?.setBackgroundColor(animVal)
            viewHolder?.cleanupAnimationListener = animCleanup
        }
        animator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                animatorsPlaying.remove(animator)
                viewAdapter.getActiveViewHolder(sound)?.also { animCleanup.onUnbind(it) }
            }
        })
        animator.duration = duration
        animator.start()
        animatorsPlaying.add(animator)
    }

    private fun playTargetedAnimator(view: View, duration: Long) {
        val animator = AnimatorInflater.loadAnimator(context, R.animator.fade)
        animator.setTarget(view)
        animator.duration = duration
        animator.start()
    }
}
