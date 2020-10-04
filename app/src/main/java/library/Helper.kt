package library

import android.content.Context
import android.content.res.Resources
import android.util.Log
import tgl.templateapp.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.util.*

object Helper {
    private const val TAG = "==== HELPER ===="
    lateinit var context: MainActivity
    private lateinit var resources: Resources
    private lateinit var properties: Properties
    private lateinit var f: File

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
                setConfigValue("screen_on", false.toString())
                setConfigValue("volume_on", true.toString())
                Log.v(TAG, properties.toString())
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open config file. " + e.message)
            //e.printStackTrace()
        }
    }

    fun getConfigValue(name: String): String? {
        return properties.getProperty(name)
    }

    fun setConfigValue(name: String, value: String) {
        properties.setProperty(name, value)
        properties.store(FileOutputStream(f), "This is an optional comment.")
    }

    fun saveProperties() {
        setConfigValue("screen_on", context.screenOn.toString())
        setConfigValue("volume_on", context.volumeOn.toString())
    }
}