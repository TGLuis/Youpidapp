package lufra.youpidapp

import android.content.Context
import android.content.res.Resources
import android.util.Log
import data.Sound
import org.json.JSONObject
import java.io.*
import java.util.*

object Helper {
    private const val TAG = "==== HELPER ===="
    lateinit var context: MainActivity
    private lateinit var resources: Resources
    private lateinit var properties: Properties
    private lateinit var f: File

    private lateinit var jsonStr: String
    private lateinit var jsonObj: JSONObject

    fun init(c: Context) {
        context = c as MainActivity
        resources = context.resources
        properties = Properties()
        try {
            val fileName = "config.properties"
            f = File(context.filesDir.path + "/" + fileName)
            if (f.exists()) {
                properties.load(FileReader(f))
            } else {
                f.setReadable(true)
                f.setWritable(true)
                f.createNewFile()
                initElements()
                Log.v(TAG, properties.toString())
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open config file. " + e.message)
            //e.printStackTrace()
        }
        initSoundDB()
    }

    private fun initElements() {
        setConfigValue("reading_type", 1.toString())
    }

    fun getConfigValue(name: String): String? {
        return properties.getProperty(name)
    }

    fun setConfigValue(name: String, value: String) {
        properties.setProperty(name, value)
        properties.store(FileOutputStream(f), "This is an optional comment.")
    }

    private fun initSoundDB() {
        jsonStr = resources.openRawResource(R.raw.aaaadb).bufferedReader().use { it.readText() }
        jsonObj = JSONObject(jsonStr)
    }

    fun getSounds(): List<Sound> {
        val jsonSoundsArray = jsonObj.getJSONArray("sounds")
        return (0 until jsonSoundsArray.length()).map { jsonSoundsArray[it] as JSONObject }.map {
            Sound(
                it.getInt("id"),
                it.getString("name"),
                it.getString("displaytext"),
            )
        }
    }
}