package data

import java.text.Normalizer

class Sound(
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

        other as Sound

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "Sound(name='$name', displayText='$displayText')"
}
