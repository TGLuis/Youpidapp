package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import data.Sound
import lufra.youpidapp.R
import java.text.Normalizer

class SoundAdapter(_soundsList: List<Sound>, private val soundClickedListener: SoundClickedListener): RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {

    private var mComparator: Comparator<Sound> = Comparator { o1, o2 ->
        /**
         * If any of the sound has a negative or zero id, then compare using the id: they are compared in inverse order
         * (i.e., id -2 > id -1). If both have a positive id, then compare using the name.
         */
        val s1 = o1!!
        val s2 = o2!!
        if (s1.id <= 0 || s2.id <= 0)
            s2.id - s1.id
        else
            s1.name.compareTo(s2.name)
    }
    private val mSoundsList = _soundsList.toMutableList()
    private val mSortedList = SortedList(Sound::class.java, object: SortedListAdapterCallback<Sound>(this) {
        override fun compare(o1: Sound?, o2: Sound?): Int {
            return mComparator.compare(o1, o2)
        }

        override fun areContentsTheSame(oldItem: Sound?, newItem: Sound?): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(item1: Sound?, item2: Sound?): Boolean {
            return item1 === item2
        }
    }).apply {
        addAll(_soundsList)
    }
    private val activeViewHolderForSound = HashMap<Sound, SoundViewHolder>()

    interface SoundClickedListener {
        fun onSoundClicked(soundViewHolder: SoundViewHolder)
    }

    /** Used for informing the front-end that the button was unbound, and thus, its properties should be restored to the initial state. */
    interface CleanupAnimationListener {
        fun onUnbind(soundViewHolder: SoundViewHolder)
    }

    // Kotlin evil machinery for lazyness ^^
    operator fun <T> SortedList<T>.iterator(): Iterator<T> {
        return object : Iterator<T> {
            private var i = 0
            override fun hasNext(): Boolean = i < this@iterator.size()
            override fun next(): T = this@iterator[i++]
        }
    }

    class SoundViewHolder(itemView: View, soundClickedListener: SoundClickedListener) : RecyclerView.ViewHolder(itemView) {

        val button: Button = itemView.findViewById(R.id.sound_button_id)
        var sound: Sound? = null
        var cleanupAnimationListener: CleanupAnimationListener? = null

        init {
            button.setOnClickListener {
                soundClickedListener.onSoundClicked(this)
            }
        }

        fun bind(sound: Sound, adapter: SoundAdapter) {
            this.sound = sound
            button.text = sound.displayText
            adapter.activeViewHolderForSound[sound] = this
        }

        fun unbind(adapter: SoundAdapter) {
            adapter.activeViewHolderForSound.remove(sound)
            cleanupAnimationListener?.onUnbind(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sound_button, parent, false)
        return SoundViewHolder(view, soundClickedListener)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bind(mSortedList[position], this)
    }

    override fun onViewRecycled(holder: SoundViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind(this)
    }

    fun getActiveViewHolder(sound: Sound): SoundViewHolder? {
        return activeViewHolderForSound[sound]
    }

    override fun getItemCount(): Int {
        return mSortedList.size()
    }

    private fun normalizeString(s: String): String = Normalizer.normalize(s, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "").toLowerCase()

    fun filter(query: String?) {
        if (query == null)
            return
        val queryNormalized = normalizeString(query)
        val filteredList = mutableListOf<Sound>()
        for (sound in mSoundsList)
            if (normalizeString(sound.displayText).contains(queryNormalized))
                filteredList.add(sound)
        replaceAll(filteredList)
    }

    fun remove(sounds: List<Sound>) {
        // I keep this one because it is complicated to rewrite
        mSortedList.beginBatchedUpdates()
        for (sound in sounds)
            mSortedList.remove(sound)
        mSortedList.endBatchedUpdates()
    }

    fun replaceAll(sounds: List<Sound>) {
        mSortedList.replaceAll(sounds)
    }
}
