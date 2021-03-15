package lufra.youpidapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import data.Stat
import lufra.youpidapp.Helper.context

class MyDatabase : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object Co {
        private const val DATABASE_NAME = "youpidb.sqlite"
        private const val DATABASE_VERSION = 1
        
        object Favorites : BaseColumns {
            const val TABLE_NAME = "favorites"
            const val COLUMN_NAME_FAVORITE_NAME = "favori"
            val ALL_COLUMNS = arrayOf(COLUMN_NAME_FAVORITE_NAME)
        }
        object Stats_data : BaseColumns {
            const val TABLE_NAME = "stats_data"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_VALUE = "value"
            val ALL_COLUMNS = arrayOf(COLUMN_NAME_NAME, COLUMN_NAME_VALUE)
        }
        object Stats_category : BaseColumns {
            const val TABLE_NAME = "stats_categories"
            const val COLUMN_NAME_CATEGORY_NAME = "category_name"
            const val COLUMN_NAME_DATA_NAME = "data_name"
            val ALL_COLUMNS = arrayOf(COLUMN_NAME_CATEGORY_NAME)
        }
        object Achievements : BaseColumns {
            const val TABLE_NAME = "achievements"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_SUCCEEDED = "succeeded"
            const val COLUMN_NAME_DESCRIPTION = "description"
            const val COLUMN_NAME_LIEN_ICON = "lien_icon"
            const val COLUMN_NAME_DATUM = "datum"
            val ALL_COLUMNS = arrayOf(COLUMN_NAME_NAME, COLUMN_NAME_SUCCEEDED, COLUMN_NAME_DESCRIPTION, COLUMN_NAME_LIEN_ICON)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE IF NOT EXISTS \"${Favorites.TABLE_NAME}\" (" +
                "\"${Favorites.COLUMN_NAME_FAVORITE_NAME}\" TEXT NOT NULL PRIMARY KEY" +
                ");")
        db.execSQL("CREATE TABLE IF NOT EXISTS \"${Stats_data.TABLE_NAME}\" (" +
                "\"${Stats_data.COLUMN_NAME_NAME}\" TEXT NOT NULL," +
                "\"${Stats_data.COLUMN_NAME_VALUE}\" NUMERIC NOT NULL," +
                "PRIMARY KEY (\"${Stats_data.COLUMN_NAME_NAME}\")," +
                ");")
        db.execSQL("CREATE TABLE IF NOT EXISTS \"${Stats_category.TABLE_NAME}\" (" +
                "\"${Stats_category.COLUMN_NAME_CATEGORY_NAME}\" TEXT NOT NULL," +
                "\"${Stats_category.COLUMN_NAME_DATA_NAME}\" TEXT NOT NULL," +
                "PRIMARY KEY(\"${Stats_category.COLUMN_NAME_CATEGORY_NAME}\", \"${Stats_category.COLUMN_NAME_DATA_NAME}\")," +
                "FOREIGN KEY(\"${Stats_category.COLUMN_NAME_DATA_NAME}\") REFERENCES \"${Stats_data.TABLE_NAME}\" (\"${Stats_data.COLUMN_NAME_NAME}\")" +
                ");")
        db.execSQL("CREATE TABLE \"${Achievements.TABLE_NAME}\" (" +
                "\"${Achievements.COLUMN_NAME_NAME}\" TEXT NOT NULL," +
                "\"${Achievements.COLUMN_NAME_SUCCEEDED}\" INTEGER NOT NULL DEFAULT 0," +
                "\"${Achievements.COLUMN_NAME_DESCRIPTION}\" TEXT NOT NULL," +
                "\"${Achievements.COLUMN_NAME_LIEN_ICON}\" INTEGER NOT NULL," +
                "\"${Achievements.COLUMN_NAME_DATUM}\" TEXT," +
                "PRIMARY KEY(\"${Achievements.COLUMN_NAME_NAME}\")" +
                ");")
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db!!.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        this.onCreate(db)
        db!!.close()
    }

    private fun deleteDb(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS '$DATABASE_NAME';")
    }

    // FAVORITES

    fun getFavorites(): ArrayList<String> {
        val db = this.readableDatabase
        val query = "SELECT ${Favorites.COLUMN_NAME_FAVORITE_NAME} FROM '${Favorites.TABLE_NAME}';"
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
        val values = ContentValues().apply {
            put(Favorites.COLUMN_NAME_FAVORITE_NAME, sound_name)
        }
        db.insert(Favorites.TABLE_NAME, null, values)
        db.close()
    }

    fun delFavorites(sound_name: String) {
        val db = this.writableDatabase
        db.delete(Favorites.TABLE_NAME, "${Favorites.COLUMN_NAME_FAVORITE_NAME}='$sound_name'", null)
        db.close()
    }

    // STATS_DATA

    fun getInfo(db: MyDatabase, searchedName: String) : Stat {
        val rdb = db.readableDatabase
        val cursor = rdb.query(Stats_data.TABLE_NAME, Stats_data.ALL_COLUMNS,
                "${Stats_data.COLUMN_NAME_NAME}= ?",
                arrayOf(searchedName), null, null, null)

        val ans: Stat = if (cursor.moveToFirst()) {
            Stat(cursor.getString(0), cursor.getDouble(1), cursor.getString(2))
        } else Stat(Stat.ERROR_NOT_FOUND, 0.0, "");
        cursor.close()
        rdb.close()
        return ans
    }

    fun getCategoryName(db: MyDatabase, searchedName: String) : String {
        val rdb = db.readableDatabase
        val cursor = rdb.query(Stats_category.TABLE_NAME,
                arrayOf(Stats_category.COLUMN_NAME_CATEGORY_NAME), "${Stats_category.COLUMN_NAME_DATA_NAME} = ?",
                arrayOf(searchedName), null, null, null)

        val ans: String = if (cursor.moveToFirst()) {
            cursor.getString(0)
        } else Stat.ERROR_NOT_FOUND

        cursor.close()
        rdb.close()
        return ans
    }

    fun getAllStats(db: MyDatabase) : List<Stat> {
        val rdb = db.readableDatabase
        val cursor = rdb.query(Stats_data.TABLE_NAME, Stats_data.ALL_COLUMNS,
                null, null, null, null, null)

        val stats = ArrayList<Stat>()
        if (cursor.moveToFirst()) {
            do {
                stats.add(Stat(cursor.getString(0), cursor.getDouble(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        rdb.close()

        return stats
    }

    fun getTop3(db: MyDatabase): List<Stat> {
        val rdb = db.readableDatabase

        val top3Stats = ArrayList<Stat>()
        val cursor = rdb.query(Stats_data.TABLE_NAME, Stats_data.ALL_COLUMNS,
                null, null, null, null, "${Stats_data.COLUMN_NAME_VALUE} DESC", "3")
        if (cursor.moveToFirst()) {
            do {
                top3Stats.add(Stat(cursor.getString(0), cursor.getDouble(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        rdb.close()

        return top3Stats
    }

    // POST

    fun insert(db: MyDatabase, newStat: Stat) {
        val wdb = db.writableDatabase
        val values = newStat.toContentValues()
        wdb.insert(Stats_data.TABLE_NAME, null, values)
        wdb.close()
    }

    fun insert(db: MyDatabase, newStats: List<Stat>) {
        val wdb = db.writableDatabase
        wdb.beginTransaction()
        for (stat in newStats) {
            wdb.insert(Stats_data.TABLE_NAME, null, stat.toContentValues())
        }
        wdb.endTransaction()
        wdb.close()
    }

    // UPDATE

    fun update(db: MyDatabase, searchedName: String, newValue: Int) {
        val wdb = db.writableDatabase
        val values = ContentValues().apply {
            put(Stats_data.COLUMN_NAME_VALUE, newValue)
        }
        wdb.update(Stats_data.TABLE_NAME, values, "name = ?", arrayOf(searchedName))
        wdb.close()
    }

    // STATISTIC DATA
}