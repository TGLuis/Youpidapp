package data

import lufra.youpidapp.Helper

// Pretty much done
class Sound(val id: Int, val name: String) {
    val displayText: String
        get() {
            val id = Helper.context.resources.getIdentifier(name, "string", "lufra.youpidapp")
            if (id == 0) return "No DisplayText"
            return Helper.context.resources.getString(id)
        }
    companion object {
        val ALL_SOUNDS_STR = listOf(
            "aiou", "aucune_chance", "biiiiiiiyoup", "les_bonnes_series", "ca_va_prendre_du_temps",
            "ca_va_vous_faire_mal", "charles_pecheur", "complique", "donald", "do_not_worry",
            "doyen_verleyzen", "electeurs", "elimination", "eliodirupo", "energie_cinetique",
            "ils_ont_le_visage_frappe_par_la_douleur", "ikea", "il_est_mort", "it_must_work", "jean_didou",
            "le_prof_est_con", "mettez_vos_masques", "minipub", "monstre_terrifiant", "monstrueusement_long",
            "ohoui", "on_aime_pas_bis", "on_va_manger", "piece_of_cake", "pilou_calme",
            "pilou_grandiloquent", "piou_piou", "piou_piou_triste", "plein_plein_plein", "podcast",
            "premiere_ministre", "puree", "regarder_nouvelles", "singular_matrix", "sprouuuuuutch",
            "t10_minutes", "t11_minutes", "t1_minute", "t8_minutes", "temps_dingue",
            "tintin", "vilain_vilain", "virus2", "voila_voila", "youpidou",
            "youpidou_calme", "youtube", "youtubehein", "youtubepointcom", "youtubepointcomhein"
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sound

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}
