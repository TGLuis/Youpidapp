package lufra.youpidapp

import android.content.Context
import android.media.MediaPlayer
import java.util.*
import kotlin.collections.HashMap

class Discotheque(private val context: Context) {

    private val all = HashMap<String, Int>()
    private val random = Random()
    private fun <T,U> Map<T,U>.random(): Map.Entry<T,U> = entries.elementAt(random.nextInt(size))

    init {
        all["pilou"] = R.raw.pilou
        all["aucune_chance"] = R.raw.aucune_chance
        all["biiiiiiiyoup"] = R.raw.biiiiiiiyoup
        all["ca_va_prendre_du_temps"] = R.raw.ca_va_prendre_du_temps
        all["charles_pecheur"] = R.raw.charles_pecheur
        all["complique"] = R.raw.complique
        all["doyen_verleyzen"] = R.raw.doyen_verleyzen
        all["elimination"] = R.raw.elimination
        all["eliodirupo"] = R.raw.eliodirupo
        all["ikea"] = R.raw.ikea
        all["il_est_mort"] = R.raw.il_est_mort
        all["it_must_work"] = R.raw.it_must_work
        all["jean_didou"] = R.raw.jean_didou
        all["le_prof_est_con"] = R.raw.le_prof_est_con
        all["minipub"] = R.raw.minipub
        all["monstre_terrifiant"] = R.raw.monstre_terrifiant
        all["monstrueusement_long"] = R.raw.monstrueusement_long
        all["on_va_manger"] = R.raw.on_va_manger
        all["pilou_calme"] = R.raw.pilou_calme
        all["pilou_grandiloquent"] = R.raw.pilou_grandiloquent
        all["piou_piou"] = R.raw.piou_piou
        all["piou_piou_triste"] = R.raw.piou_piou_triste
        all["plein_plein_plein"] = R.raw.plein_plein_plein
        all["premiere_ministre"] = R.raw.premiere_ministre
        all["puree"] = R.raw.puree
        all["regarder_nouvelles"] = R.raw.regarder_nouvelles
        all["t11_minutes"] = R.raw.t11_minutes
        all["t1_minute"] = R.raw.t1_minute
        all["t8_minutes"] = R.raw.t8_minutes
        all["temps_dingue"] = R.raw.temps_dingue
        all["tintin"] = R.raw.tintin
        all["vilain_vilain"] = R.raw.vilain_vilain
        all["youpidou"] = R.raw.youpidou
        all["youpidou_calme"] = R.raw.youpidou_calme
        all["youtube"] = R.raw.youtube
        all["youtubehein"] = R.raw.youtubehein
        all["youtubepointcom"] = R.raw.youtubepointcom
        all["youtubepointcomhein"] = R.raw.youtubepointcomhein
    }


    fun play(name: String) {
        MediaPlayer.create(context, all[name]!!).start()
    }

    fun play_random() {
        MediaPlayer.create(context, all.random().value).start()
    }
}
