package lufra.youpidapp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import data.Sound
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Properties

object DataPersistenceHelper {
    private const val TAG = "==== HELPER ===="
    private const val fileName = "config.properties"
    private const val PLAYTYPE = "PlayType"
    private const val PITCH = "Pitch"
    private const val OPEN_ON_FAVORITES = "OpenOnFavorites"
    private const val NIGHTMODE_ACTIVATED = "NightModeActivated"
    private lateinit var f: File
    private lateinit var youpiDB: MyDatabase

    private lateinit var jsonStr: String
    private lateinit var jsonObj: JSONObject
    private lateinit var preferredSounds: ArrayList<String>
    private lateinit var sharedPref: SharedPreferences

    /**
     * Initialize the data persistence helper: Load the sharedPreferences file in memory,
     * load the SQLite database into memory and load the favorites sounds
     * @param context an Activity
     */
    fun init(context: Activity) {
        sharedPref = context.getSharedPreferences("Youpidapp_preferences", Context.MODE_PRIVATE)
        try {
            f = File(context.filesDir.path + "/" + fileName)
            if (f.exists()) {
                val properties = Properties()
                val fReader = FileReader(f)
                properties.load(fReader)
                val newReadingType = when (properties.getProperty("reading_type", "0")) {
                    "1" -> PlayType.SINGLE.name
                    "2" -> PlayType.PARALLEL.name
                    else -> PlayType.SEQUENTIAL.name
                }
                sharedPref.edit().putString(PLAYTYPE, newReadingType).apply()
                fReader.close()
                f.delete()
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open config file. " + e.message)
        }
        youpiDB = MyDatabase(context)
        preferredSounds = youpiDB.getFavorites()
        initSoundDB(context)
    }

    private fun initSoundDB(context: Activity) {
        jsonStr = context.resources.openRawResource(R.raw.aaaadb).bufferedReader().use { it.readText() }
        jsonObj = JSONObject(jsonStr)
    }

    // Properties get/set

    fun getPreferredPitch(): Float {
        return sharedPref.getFloat(PITCH, 1.0f)
    }

    fun setPreferredPitch(preferredPitch: Float) {
        sharedPref.edit().putFloat(PITCH, preferredPitch).apply()
    }

    fun getPreferredPlayType(): PlayType {
        val type = sharedPref.getString(PLAYTYPE, null)?: PlayType.SINGLE.name
        return PlayType.valueOf(type)
    }

    fun setPreferredPlayType(preferredPlayType: PlayType) {
        sharedPref.edit().putString(PLAYTYPE, preferredPlayType.name).apply()
    }

    private fun isFavouriteSound(sound_name: String): Boolean {
        return preferredSounds.contains(sound_name)
    }

    fun addSoundFavourite(sound: Sound) {
        if (sound.isFavourite)
            return
        sound.isFavourite = true
        preferredSounds.add(sound.name)
        youpiDB.addFavorite(sound.name)
    }

    fun removeSoundFavorite(sound: Sound) {
        if (!sound.isFavourite)
            return
        sound.isFavourite = false
        preferredSounds.remove(sound.name)
        youpiDB.delFavorites(sound.name)
    }

    fun shouldOpenOnFavorites(): Boolean {
        return sharedPref.getBoolean(OPEN_ON_FAVORITES, false)
    }

    fun openOnFavorites(openOnFavorites: Boolean) {
        sharedPref.edit().putBoolean(OPEN_ON_FAVORITES, openOnFavorites).apply()
    }

    fun activateNightMode() {
        sharedPref.edit().putBoolean(NIGHTMODE_ACTIVATED, true).apply()
    }

    fun deactivateNightMode() {
        sharedPref.edit().putBoolean(NIGHTMODE_ACTIVATED, false).apply()
    }

    fun isNightModeActivated() : Boolean {
        return sharedPref.getBoolean(NIGHTMODE_ACTIVATED, false)
    }

    fun getSounds(): List<Sound> {
        val jsonSoundsArray = jsonObj.getJSONArray("sounds")
        return (0 until jsonSoundsArray.length()).map { jsonSoundsArray[it] as JSONObject }.map {
            val name = it.getString("name")
            Sound(
                name,
                it.getString("displaytext"),
                it.getString("transcript"),
                isFavouriteSound(name),
            )
        }
    }
}