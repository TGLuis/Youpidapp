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

class SoundAdapter(_SoundsList: List<Sound>, private val listener: Listener): RecyclerView.Adapter<SoundAdapter.SoundHolder>() {
    companion object {
        val TAG = "Youpidadd::SoundAdapter"
    }
    private val soundsList: MutableList<Sound> = _SoundsList.toMutableList()

    interface Listener {
        fun onSoundClicked(soundHolder: SoundHolder)
    }

    class SoundHolder(itemView: View, listener: Listener) : RecyclerView.ViewHolder(itemView) {
        var sound: Sound? = null
        val button: Button = itemView.findViewById(R.id.sound_button_id)
        init {
            button.setOnClickListener {
                listener.onSoundClicked(this)
            }
        }
    }

    // Class used when we use data binding
    /*class SoundHolder(val mBinding: SoundButtonBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Sound) {
            mBinding.model = item
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sound_button, parent, false)
        return SoundHolder(view, listener)
        // If using data binding, do this:
        /* val binding = SoundButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundHolder(binding) */
    }

    override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        val sound = soundsList[position]
        holder.sound = sound
        holder.button.text = sound.displayText
        // If using data binding, use this instead:
        // holder.bind(sounds[position])
        Log.i(TAG, "VH bound $holder ${holder.button}")
    }

    override fun onViewRecycled(holder: SoundHolder) {
        super.onViewRecycled(holder)
        Log.i(TAG, "VH recycled $holder")
        val anim = holder.button.animation
        Log.i(TAG, "$anim")
    }

    fun add(sound: Sound) {
        soundsList.add(sound)
    }

    fun add(sounds: List<Sound>) {
        soundsList.addAll(sounds)
    }

    fun remove(sound: Sound) {
        soundsList.remove(sound)
    }

    fun remove(sounds: List<Sound>) {
        soundsList.removeAll(sounds) // Maybe needs to use another method
    }

    override fun getItemCount(): Int {
        return soundsList.size
    }
}
