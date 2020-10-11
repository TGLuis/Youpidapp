package lufra.youpidapp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap


class Discotheque(private val context: Context) {

    private val all = HashMap<String, Int>()
    private val random = Random()
    private fun <T, U> Map<T, U>.random(): Map.Entry<T, U> = entries.elementAt(random.nextInt(size))
    private var reading: LinkedList<MediaPlayer> = LinkedList()
    private var playlist: LinkedList<Int> = LinkedList()
    private var type = 1 /* 1 = 1 song at a time, 2 = stack songs, 3 = playlist*/

    init {
        val fields: Array<Field> = R.raw::class.java.fields
        for (f in fields) {
            all[f.name] = f.getInt(f)
        }
    }

    fun setType(newType: Int) {
        type = newType
    }

    fun getType(): Int {
        return type
    }

    fun getTypes(): Array<String> {
        return arrayOf(
            context.getString(R.string.type1),
            context.getString(R.string.type2),
            context.getString(R.string.type3))
    }

    fun play(name: String): Int {
        try {
            when (type) {
                1 -> {
                    return playOne(name)
                }
                2 -> {
                    return playStack(name)
                }
                3 -> {
                    return playList(name)
                }
            }
        } catch (e: java.lang.IllegalStateException) {
            reading.clear()
            playlist.clear()
        }
        return 0
    }

    private fun getPlayer(id: Int): MediaPlayer {
        val mp = MediaPlayer.create(context, id)
        mp.setOnErrorListener { _, _, _ ->
            stopAll()
            reading.clear()
            true
        }
        return mp
    }

    private fun playOne(name: String): Int {
        if (reading.size == 0) {
            val mp = getPlayer(all[name]!!)
            mp.setOnCompletionListener { mp.stop() }
            reading.add(mp)
            reading[0].start()
            return reading[0].duration
        } else if (reading.size == 1) {
            reading[0].stop()
            return changeSongAndStart(reading[0], all[name]!!)
        } else {
            stopAll()
            return playOne(name)
        }
    }

    private fun playStack(name: String): Int {
        val mp = getPlayer(all[name]!!)
        reading.add(mp)
        mp.setOnCompletionListener {
            mp.release()
            reading.remove(mp)
        }
        mp.start()
        return mp.duration
    }

    private fun playList(name: String): Int {
        if (reading.size != 1) {
            reading.clear()
            val mp = getPlayer(all[name]!!)
            reading.add(mp)
            mp.setOnCompletionListener {
                if (playlist.size > 0) {
                    mp.stop()
                    changeSongAndStart(mp, playlist.pop())
                }
            }
            mp.start()
        } else if (reading.size == 1 && !reading[0].isPlaying) {
            changeSongAndStart(reading[0], all[name]!!)
        } else {
            playlist.add(all[name]!!)
        }
        return 0
    }

    private fun changeSongAndStart(mp: MediaPlayer, id: Int): Int {
        mp.reset()
        mp.setDataSource(context, Uri.parse("android.resource://lufra.youpidapp/$id"))
        mp.prepare()
        mp.start()
        return mp.duration
    }

    fun playRandom(): Int {
        return play(all.random().key)
    }

    private fun stopIt(player: MediaPlayer) {
        if (player.isPlaying)
            player.stop()
    }

    fun stopAll() {
        reading.forEach {
            try {
                stopIt(it)
            } catch (e: java.lang.IllegalStateException) {}
        }
        reading.clear()
    }
}
