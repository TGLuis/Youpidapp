package lufra.youpidapp

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import data.Sound
import org.json.JSONObject
import java.io.*
import java.util.*

object Helper {
    private const val TAG = "==== HELPER ===="
    private const val fileName = "config.properties"
    lateinit var context: Activity
    private lateinit var resources: Resources
    private lateinit var properties: Properties
    private lateinit var f: File

    private lateinit var jsonStr: String
    private lateinit var jsonObj: JSONObject

    fun init(c: Context) {
        context = c as Activity
        resources = context.resources
        properties = Properties()
        try {
            f = File(context.filesDir.path + "/" + fileName)
            if (f.exists()) {
                val freader = FileReader(f)
                properties.load(freader)
                freader.close()
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
        }
        initSoundDB()
    }

    fun restart(c: Context) {
        try {
            f = File(context.filesDir.path + "/" + fileName)
            if (f.exists()) {
                f.delete()
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find the config file: " + e.message)
        }
        this.init(c)
    }

    private fun initElements() {
        setConfigValue("reading_type", 1.toString())
    }

    fun getConfigValue(name: String): String? {
        return properties.getProperty(name)
    }

    fun setConfigValue(name: String, value: String) {
        properties.setProperty(name, value)
        val outputStream = FileOutputStream(f)
        properties.store(outputStream, "This is an optional comment.")
        outputStream.close()
    }

    private fun initSoundDB() {
        jsonStr = resources.openRawResource(R.raw.aaaadb).bufferedReader().use { it.readText() }
        jsonObj = JSONObject(jsonStr)
    }

    fun getSounds(): List<Sound> {
        val jsonSoundsArray = jsonObj.getJSONArray("sounds")
        return (0 until jsonSoundsArray.length()).map { jsonSoundsArray[it] as JSONObject }.map {
            Sound(
                it.getString("name"),
                it.getString("displaytext"),
            )
        }
    }
}