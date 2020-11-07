package adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import data.Sound
import lufra.youpidapp.R
// Import used when using data binding
//import lufra.youpidapp.databinding.SoundButtonBinding

class SoundAdapter(_SoundsList: List<Sound>, private val listener: Listener): RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {

    /*interface Animation {
        fun pause(soundViewHolder: SoundViewHolder)
        fun resume(soundViewHolder: SoundViewHolder)
    }*/

    /*class SoundHolder(val sound: Sound) {
        var animation: Animation? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SoundHolder

            if (sound != other.sound) return false

            return true
        }
        override fun hashCode(): Int {
            return sound.hashCode()
        }
    }*/

    companion object {
        val TAG = "Youpidapp::SoundAdapter"
    }

    private val soundsList: MutableList<Sound> = _SoundsList.toMutableList()

    interface Listener {
        fun onSoundClicked(soundViewHolder: SoundViewHolder)
    }

    class SoundItemAnimation(val animatorGenerator: (SoundViewHolder) -> Animator): DefaultItemAnimator() {
        companion object {
            val SOUND_CLIKED = 426901
            val TAG = SoundAdapter.TAG + "::SoundItemAnimation"
        }

        class SoundItemHolderInfo(val clicked: Boolean): ItemHolderInfo() {
        }

        override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
            Log.i(TAG, "canReuseUpdatedViewHolder(VH=$viewHolder)")
            return true
        }

        override fun canReuseUpdatedViewHolder(
            viewHolder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ): Boolean {
            Log.i(TAG, "canReuseUpdatedViewHolder(VH=$viewHolder, payload=$payloads")
            return true
        }

        override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preInfo: ItemHolderInfo,
            postInfo: ItemHolderInfo
        ): Boolean {
            val holder = newHolder as SoundViewHolder
            if (preInfo is SoundItemHolderInfo) {
                assert(oldHolder == newHolder) { "Different holders, not good" }
                if (preInfo.clicked) {  // TODO check below, the meaning of this is ill-defined
                    val animator = animatorGenerator(holder)
                    animator.addListener(object: AnimatorListenerAdapter() {
                        val TAG = SoundItemAnimation.TAG + "::animateChange::AnimatorListenerAdapter"
                        override fun onAnimationEnd(animation: Animator?) {
                            Log.i(TAG, "onAnimEnd, $animation, $holder")
                            dispatchAnimationFinished(holder)
                        }
                    })
                    animator.setTarget(holder.button)
                    animator.start()
                }
                return true
            }
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }

        override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int,
            payloads: MutableList<Any>
        ): ItemHolderInfo {
            if (changeFlags == FLAG_CHANGED)
                for (payload in payloads)
                    if (payload as? Int == SOUND_CLIKED)
                        return SoundItemHolderInfo(true)  // TODO actually here I should check if the animation has started or not, to replace it by a new animation.
            return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
        }
    }

    class SoundViewHolder(itemView: View, listener: Listener) : RecyclerView.ViewHolder(itemView) {

        val button: Button = itemView.findViewById(R.id.sound_button_id)
        var sound: Sound? = null

        init {
            button.setOnClickListener {
                listener.onSoundClicked(this)
            }
        }

        fun bind(sound: Sound) {
            this.sound = sound
            button.text = sound.displayText
            //soundHolder?.animation?.resume(this)
        }

        fun unbind() {
            //soundHolder?.animation?.pause(this)
        }

        /*fun addAnimation(anim: Animation) {
            sound?.animation = anim
        }

        fun removeAnimation(anim: Animation) {
            if (sound?.animation == anim) {
                sound?.animation = null
            }
        }*/
    }

    // Class used when we use data binding
    /*class SoundHolder(val mBinding: SoundButtonBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Sound) {
            mBinding.model = item
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sound_button, parent, false)
        return SoundViewHolder(view, listener)
        // If using data binding, do this:
        /* val binding = SoundButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundHolder(binding) */
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bind(soundsList[position])
        Log.i(TAG, "VH bound $holder ${holder.button}")
    }

    override fun onViewRecycled(holder: SoundViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
        Log.i(TAG, "VH recycled $holder")
    }

    override fun onViewAttachedToWindow(holder: SoundViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.i(TAG, "VH attached $holder")
    }

    override fun onViewDetachedFromWindow(holder: SoundViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.i(TAG, "VH detached $holder")
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
        soundsList.removeAll(sounds)  // Maybe needs to use another method
    }

    override fun getItemCount(): Int {
        return soundsList.size
    }
}
