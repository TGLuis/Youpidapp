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
import fragments.adapter.Sound
import fragments.adapter.SoundAdapter
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
                val button = soundHolder.button
                val soundName = soundHolder.sound!!.name
                val duration = context.discotheque.play(soundName)
                addAnimator(button, duration.toLong())
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

        val parent = context.findViewById<LinearLayout>(R.id.list_of_buttons)
        iterateOverChildrenAndSetButtons2(parent)
        context.setMenu("home")
    }

    private fun iterateOverChildrenAndSetButtons2(parent: LinearLayout) {
        parent.children.forEach { it ->
            if (it::class == LinearLayout::class) {
                iterateOverChildrenAndSetButtons2(it as LinearLayout)
            } else {
                val idName = resources.getResourceEntryName(it.id)
                if (idName == "random") {
                    it.setOnClickListener { button ->
                        val duration = context.discotheque.playRandom()
                        addAnimator(button, duration.toLong())
                    }
                }
            }
        }
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
