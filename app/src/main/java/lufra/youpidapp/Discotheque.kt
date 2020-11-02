package lufra.youpidapp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
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

    fun toastOfType() {
        Toast.makeText(context, "Mode de lecture: " + getTypes()[getType()-1], Toast.LENGTH_LONG).show()
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

    /**
     * Play a random pilou sound
     */
    fun playRdmPilou(): Int {
        val n = Math.random()
        if (n < 0.2) {
           return play("pilou")
        } else if(n < 0.4) {
            return play("pilou_calme")
        } else if(n < 0.6) {
            return play("pilou_concentre")
        } else if(n < 0.8) {
            return play("pilou_grandiloquent")
        } else {
            return play("pilou_rapide")
        }
    }

    /**
     * Play a random piou sound
     */
    fun playRdmPiou(): Int {
        val n = Math.random()
        if (n < 0.5) {
            return play("piou_piou")
        } else {
            return play("piou_piou_triste")
        }
    }

    /**
     * Play a random youpidou sound
     */
    fun playRdmYoupidou(): Int {
        val n = Math.random()
        if (n < 0.5) {
            return play("youpidou")
        } else {
            return play("youpidou_calme")
        }
    }

    /**
     * Play a random youtube sound
     */
    fun playRdmYoutube(): Int {
        val n = Math.random()
        if (n < 0.25) {
            return play("youtube")
        } else if (n < 0.5) {
            return play("youtubehein")
        } else if (n < 0.75) {
            return play("youtubepointcom")
        } else {
            return play("youtubepointcomhein")
        }
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
        Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show()
    }
}
