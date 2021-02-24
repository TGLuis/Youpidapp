package lufra.youpidapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import lufra.youpidapp.Helper.context

class MyDatabase : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "youpidb.sqlite"
        private const val DATABASE_VERSION = 1

        private const val TABLE_FAVORITE = "favorites"
        private const val FAVORITE_NAME = "favori"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        this.deleteDb(db!!)
        db.execSQL("CREATE TABLE '$TABLE_FAVORITE' (" +
                "'$FAVORITE_NAME' TEXT NOT NULL PRIMARY KEY" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        this.onCreate(db)
        db!!.close()
    }

    private fun deleteDb(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS '$DATABASE_NAME';")
    }

    fun getFavorites(): ArrayList<String> {
        val db = this.writableDatabase
        val query = "SELECT $FAVORITE_NAME FROM '$TABLE_FAVORITE';"
        val cursor = db.rawQuery(query, null)
        val favorites = ArrayList<String>()
        if (cursor.moveToFirst()) {
            do {
                favorites.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return favorites
    }

    fun addFavorite(sound_name : String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.apply {
            put(FAVORITE_NAME, sound_name)
        }
        db.insert(TABLE_FAVORITE, null, values)
        db.close()
    }

    fun delFavorites(sound_name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_FAVORITE, "$FAVORITE_NAME='$sound_name'", null)
        db.close()
    }
}