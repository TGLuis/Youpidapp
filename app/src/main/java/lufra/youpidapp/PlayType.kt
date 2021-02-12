package lufra.youpidapp

enum class PlayType {
    /* SINGLE = 1 song at a time, PARALLEL = stack songs, SEQUENTIAL = playlist*/
    SINGLE,
    PARALLEL,
    SEQUENTIAL;
    companion object {
        fun fromInt(i: Int): PlayType = values()[i]
    }
}