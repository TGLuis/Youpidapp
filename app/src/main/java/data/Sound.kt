package data

import java.text.Normalizer

data class Sound(
    val name: String,
    val displayText: String,
    val transcript: String,
    var isFavourite: Boolean,
) {
    companion object {
        fun normalizeString(s: String): String = Normalizer.normalize(s, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "").toLowerCase()
        val RANDOM = Sound("random", "Random", "Random text", false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return name == (other as Sound).name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "Sound(name='$name', displayText='$displayText')"
}
