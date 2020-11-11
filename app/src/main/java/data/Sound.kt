package data

class Sound(
    val id: Int,
    val name: String,
    val displayText: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sound

        return id == other.id && name == other.name
    }

    override fun hashCode(): Int = 31 * id + name.hashCode()

    override fun toString(): String = "Sound(id=$id, name='$name')"
}
