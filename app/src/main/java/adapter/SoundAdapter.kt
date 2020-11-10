package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import data.Sound
import lufra.youpidapp.R
// Import used when using data binding
//import lufra.youpidapp.databinding.SoundButtonBinding

class SoundAdapter(_SoundsList: List<Sound>, private val soundClickedListener: SoundClickedListener): RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {

    companion object {
        const val TAG = "Youpidapp::SoundAdapter"
    }

    private val soundsList: MutableList<Sound> = _SoundsList.toMutableList()
    private val activeViewHolderForSound = HashMap<Sound, SoundViewHolder>()

    interface SoundClickedListener {
        fun onSoundClicked(soundViewHolder: SoundViewHolder)
    }

    /** Used for informing the front-end that the button was unbound, and thus, its properties should be restored to the initial state. */
    interface CleanupAnimationListener {
        fun onUnbind(soundViewHolder: SoundViewHolder)
    }

    class SoundViewHolder(itemView: View, soundClickedListener: SoundClickedListener) : RecyclerView.ViewHolder(itemView) {

        val button: Button = itemView.findViewById(R.id.sound_button_id)
        var sound: Sound? = null  // TODO replace by a call to the adapter, like the following; may fail because I don't know what is the correct position to use: adapter or layout
        /*lateinit var adapter: SoundAdapter
        val sound: Sound?
            get() {
                return adapter.soundsList[adapterPosition]
            }*/
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
            Log.d(TAG, "VH $this bind called ")
        }

        fun unbind(adapter: SoundAdapter) {
            adapter.activeViewHolderForSound.remove(sound)
            cleanupAnimationListener?.onUnbind(this)
            Log.d(TAG, "VH $this unbind called")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sound_button, parent, false)
        return SoundViewHolder(view, soundClickedListener)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bind(soundsList[position], this)
        Log.d(TAG, "VH bound $holder ${holder.button}")
    }

    override fun onViewRecycled(holder: SoundViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind(this)
        Log.d(TAG, "VH recycled $holder")
    }

    fun getActiveViewHolder(sound: Sound): SoundViewHolder? {
        return activeViewHolderForSound[sound]
    }

    override fun getItemCount(): Int {
        return soundsList.size
    }
}
