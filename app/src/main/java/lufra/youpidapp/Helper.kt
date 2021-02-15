package lufra.youpidapp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import data.Sound
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Properties

object Helper {
    private const val TAG = "==== HELPER ===="
    private const val fileName = "config.properties"
    private const val PLAYTYPE = "PlayType"
    private const val PITCH = "Pitch"
    lateinit var context: Activity
    private lateinit var f: File

    private lateinit var jsonStr: String
    private lateinit var jsonObj: JSONObject

    private lateinit var sharedPref: SharedPreferences

    fun init(c: Context) {
        context = c as Activity
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
        initSoundDB()
    }

    fun setPreferredPitch(preferredPitch: Float) {
        sharedPref.edit().putFloat(PITCH, preferredPitch).apply()
    }

    fun getPreferredPitch(): Float {
        return sharedPref.getFloat(PITCH, 1.0f)
    }

    fun setPreferredPlayType(preferredPlayType: PlayType) {
        sharedPref.edit().putString(PLAYTYPE, preferredPlayType.name).apply()
    }

    fun getPreferredPlayType(): PlayType {
        val type = sharedPref.getString(PLAYTYPE, null)?: PlayType.SINGLE.name
        return PlayType.valueOf(type)
    }

    private fun initSoundDB() {
        jsonStr = context.resources.openRawResource(R.raw.aaaadb).bufferedReader().use { it.readText() }
        jsonObj = JSONObject(jsonStr)
    }

    fun getSounds(): List<Sound> {
        val jsonSoundsArray = jsonObj.getJSONArray("sounds")
        return (0 until jsonSoundsArray.length()).map { jsonSoundsArray[it] as JSONObject }.map {
            Sound(
                it.getString("name"),
                it.getString("displaytext"),
                it.getString("transcript"),
            )
        }
    }
}