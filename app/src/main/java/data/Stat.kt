package data

import android.content.ContentValues
import lufra.youpidapp.MyDatabase

class Stat(var name: String, var value: Double, var category: String) {
    companion object LiaisonDB {
        const val ERROR_NOT_FOUND = "Error_stat_not_found"
        val STATIC_S_D = MyDatabase.Co.Stats_data


    }

    fun toContentValues(): ContentValues {
        val values = ContentValues().apply {
            put(STATIC_S_D.COLUMN_NAME_NAME, name)
            put(STATIC_S_D.COLUMN_NAME_VALUE, value)
        }
        return values
    }
}