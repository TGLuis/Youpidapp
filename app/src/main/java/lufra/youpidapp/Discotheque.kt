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

    fun getTypes(): Array<String> {
        return arrayOf(
            context.getString(R.string.type1),
            context.getString(R.string.type2),
            context.getString(R.string.type3))
    }

    fun play(name: String) {
        try {
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
        } catch (e: java.lang.IllegalStateException) {
            //stopAll()
            reading.clear()
            playlist.clear()
        }
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

    private fun playOne(name: String) {
        if (reading.size != 0) {
            stopIt(reading[0])
            reading[0].release()
            reading.clear()
        }
        val mp = getPlayer(all[name]!!)
        mp.setOnCompletionListener { mp.release() }
        reading.add(mp)
        reading[0].start()
    }

    private fun playStack(name: String) {
        val mp = getPlayer(all[name]!!)
        reading.add(mp)
        mp.setOnCompletionListener {
            mp.release()
            reading.remove(mp)
        }
        mp.start()
    }

    private fun playList(name: String) {
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
    }

    private fun changeSongAndStart(mp: MediaPlayer, id: Int) {
        mp.reset()
        mp.setDataSource(context, Uri.parse("android.resource://lufra.youpidapp/$id"))
        mp.prepare()
        mp.start()
    }

    fun playRandom() {
        play(all.random().key)
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
