package library

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("DROP TABLE IF EXISTS '$DATABASE_NAME';")
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val db = this.writableDatabase
        this.deleteDb(db)
        this.onCreate(db)
    }

    fun deleteDb(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS '$DATABASE_NAME';")
    }

    companion object {
        private const val TAG = "===== MYDATABASE ====="
        private const val DATABASE_NAME = "database.sqlite"
        private const val DATABASE_VERSION = 1

        private const val NONE = "__--NO--__"
    }
}
