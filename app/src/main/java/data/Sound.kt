package data

class Sound(
    val name: String,
    val displayText: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sound

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "Sound(name='$name', displayText='$displayText')"
}