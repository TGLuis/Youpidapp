package lufra.youpidapp

import android.content.Context
import android.media.MediaPlayer
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap


class Discotheque(private val context: Context) {

    private val all = HashMap<String, Int>()
    private val random = Random()
    private fun <T, U> Map<T, U>.random(): Map.Entry<T, U> = entries.elementAt(random.nextInt(size))
    private var reading: LinkedList<MediaPlayer> = LinkedList()
    private var type = 2 /* 1 = 1 song at a time, 2 = stack songs, 3 = playlist*/

    init {
        val fields: Array<Field> = R.raw::class.java.fields
        for (f in fields) {
            all[f.name] = f.getInt(f)
        }
    }

    fun play(name: String) {
        when (type) {
            1 -> {
                playOne(name)
            }
            2 -> {
                playStack(name)
            }
            3 -> {
                playList(name)
            }
        }
    }

    private fun playOne(name: String) {
        if (reading.size != 0) {
            stopIt(reading[0])
            reading[0].release()
            reading.clear()
        }
        reading.add(MediaPlayer.create(context, all[name]!!))
        reading[0].start()
    }

    private fun playStack(name: String) {
        val mp = MediaPlayer.create(context, all[name]!!)
        reading.add(mp)
        mp.setOnCompletionListener {
            mp.release()
            reading.remove(mp)
        }
        mp.start()
    }

    private fun playList(name: String) {
        val mp = MediaPlayer.create(context, all[name]!!)
        reading.add(mp)
        mp.setOnCompletionListener {
            mp.release()
            reading.remove(mp)
            if (reading.size > 0)
                reading[0].start()
        }
        if (!reading[0].isPlaying)
            reading[0].start()
    }

    fun playRandom() {
        play(all.random().key)
    }

    private fun stopIt(player: MediaPlayer) {
        if (player.isPlaying)
            player.stop()
    }

    fun stopAll() {
        reading.forEach {stopIt(it)}
    }
}
