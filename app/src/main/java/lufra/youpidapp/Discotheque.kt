package lufra.youpidapp

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.widget.Toast
import java.lang.reflect.Field
import java.util.LinkedList
import kotlin.collections.HashMap
import kotlin.random.Random


class Discotheque(private val context: Context) {

    private val PILOU_ARRAY: Array<String> = arrayOf(
        "pilou", "pilou_calme", "pilou_concentre",
        "pilou_grandiloquent", "pilou_rapide", "pilou_long", "pilou_attentif", "pilou_explicatif",
            "pilou_attention", "pilou_fin", "pilou_ponctuation"
    )
    private val PIOU_ARRAY: Array<String> = arrayOf("piou_piou", "piou_piou_triste")
    private val YOUPIDOU_ARRAY: Array<String> = arrayOf("youpidou", "youpidou_calme")
    private val YOUTUBE_ARRAY: Array<String> = arrayOf(
        "youtube", "youtubehein", "youtubepointcom", "youtubepointcomhein"
    )
    private val CASTE_SUPERIEURE: String = "epl"
    private val CASTE_INFERIEURE: Array<String> = arrayOf("comu", "help")
    private val MONSTRE_TERRIFIANT: String = "monstre_terrifiant"
    private val ELIMINATION: String = "elimination"
    // musiques (on stock ça dans un array c'est plus facile
    private val MUSICS_NAMES: Array<String> = arrayOf("heartbreaking_by_kevin_macleod_from_filmmusic_io",
            "pluie", "ice_flow_by_kevin_macleod_from_filmmusic_io", "too_cool_by_kevin_macleod_from_filmmusic_io.mp3")
    private val BRUITAGES: Array<String> = arrayOf("hacking")

    private val all = HashMap<String, Int>()
    private val musics = HashMap<String, Int>()
    private fun <T, U> Map<T, U>.random(): Map.Entry<T, U> = entries.elementAt(Random.nextInt(size))
    private var reading: LinkedList<MediaPlayer> = LinkedList()
    private var playlist: LinkedList<Int> = LinkedList()
    private var type = PlayType.SINGLE /* 1 = 1 song at a time, 2 = stack songs, 3 = playlist*/
    private var params: PlaybackParams

    init {
        val fields: Array<Field> = R.raw::class.java.fields
        for (f in fields) {
            // comme ça on mélange pas tout
            if (MUSICS_NAMES.contains(f.name)) {
                musics[f.name] = f.getInt(f)
            } else {
                all[f.name] = f.getInt(f)
            }
        }

        params = PlaybackParams()
        params.pitch = 1.0f
    }

    fun setType(newType: PlayType) {
        type = newType
    }

    fun setPitch(newPitch: Float) {
        if (newPitch < 0.5f) {
            params.pitch = 0.5f
        } else if (newPitch > 2.0f) {
            params.pitch = 2.0f
        } else {
            params.pitch = newPitch
        }
    }

    fun getPitch(): Float {
        return params.pitch
    }

    fun getDefaultPitch(): Float {
        return 1f
    }

    fun getPlayType(): PlayType {
        return type
    }

    fun getTypes(): Array<String> {
        return context.resources.getStringArray(R.array.types)
    }

    fun play(name: String): Int {
        try {
            when (type) {
                PlayType.SINGLE -> return playOne(all[name]!!)
                PlayType.PARALLEL -> return playStack(all[name]!!)
                PlayType.SEQUENTIAL -> return playList(all[name]!!)
            }
        } catch (e: java.lang.IllegalStateException) {
            reading.clear()
            playlist.clear()
        }
        return 0
    }

    private fun getPlayer(id: Int): MediaPlayer {
        val mp = MediaPlayer.create(context, id)
        mp.playbackParams = params
        mp.setOnErrorListener { _, _, _ ->
            stopAll()
            reading.clear()
            true
        }
        return mp
    }

    /**
     * Joue le son "name" en mode "un seul à la fois".
     * @param soundId id de resource de son (valide).
     */
    private fun playOne(soundId: Int): Int {
        if (reading.size == 0) {
            val mp = getPlayer(soundId)
            mp.setOnCompletionListener { mp.stop() }
            reading.add(mp)
            reading[0].playbackParams.pitch = params.pitch
            reading[0].start()
            return reading[0].duration
        } else if (reading.size == 1) {
            reading[0].stop()
            return changeSongAndStart(reading[0], soundId)
        } else {
            stopAll()
            return playOne(soundId)
        }
    }

    private fun playStack(soundId: Int): Int {
        val mp = getPlayer(soundId)
        reading.add(mp)
        mp.setOnCompletionListener {
            mp.release()
            reading.remove(mp)
        }
        mp.playbackParams.pitch = params.pitch
        mp.start()
        return mp.duration
    }

    private fun playList(soundId: Int): Int {
        if (reading.size != 1) {
            reading.clear()
            val mp = getPlayer(soundId)
            reading.add(mp)
            mp.setOnCompletionListener {
                if (playlist.size > 0) {
                    mp.stop()
                    changeSongAndStart(mp, playlist.pop())
                }
            }
            mp.playbackParams.pitch = params.pitch
            mp.start()
        } else if (reading.size == 1 && !reading[0].isPlaying) {
            changeSongAndStart(reading[0], soundId)
        } else {
            playlist.add(soundId)
        }
        return 0
    }

    private fun changeSongAndStart(mp: MediaPlayer, id: Int): Int {
        mp.reset()
        mp.setDataSource(context, Uri.parse("android.resource://lufra.youpidapp/$id"))
        mp.prepare()
        mp.playbackParams = params
        mp.start()
        return mp.duration
    }

    fun playRandom(): Int {
        return play(all.random().key)
    }

    fun playRdmPilou(): Int {
        return play(PILOU_ARRAY.random())
    }

    fun playRdmPiou(): Int {
        return play(PIOU_ARRAY.random())
    }

    fun playRdmYoupidou(): Int {
        return play(YOUPIDOU_ARRAY.random())
    }

    fun playRdmYoutube(): Int {
        return play(YOUTUBE_ARRAY.random())
    }

    fun playCasteSuperieure(): Int {
        return play(CASTE_SUPERIEURE)
    }

    fun playCasteInferieure(): Int {
        val rdm = Random.nextFloat()
        if (rdm < 0.2) {
            return play((CASTE_INFERIEURE[1]))
        }
        return play(CASTE_INFERIEURE[0])
    }

    fun playMonstreTerrifiant(): Int {
        return play(MONSTRE_TERRIFIANT)
    }

    fun playMusiqueTriste(): Int {
        return playMusic(0)
    }

    fun playPluie(): Int {
        return playMusic(1)
    }

    // EXTERMINATE button

    fun playPiou(): Int {
        return play(PIOU_ARRAY[0])
    }

    fun playExterminate(): Int {
        return play(ELIMINATION)
    }

    fun playHacking(): Int {
        return play(BRUITAGES[0])
    }

    // MUSICS BUTTON

    fun getMusicList(): Array<String> {
        return MUSICS_NAMES
    }

    fun playMusic(id: Int): Int {
        if (id < 0 || id >= musics.size ) return -1
        return playStack(musics[MUSICS_NAMES[id]]!!)
    }

    // STOP

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
