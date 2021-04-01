package lufra.youpidapp

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.*
import org.json.JSONObject
import org.junit.Assert.assertNotEquals

class JsonTest {
    var jsonObject: JSONObject? = null

    /**
     * This function is executed before each test
     */
    @Before
    fun setUp() {
        if (jsonObject == null) {
            val aaaadbFile = javaClass.classLoader?.getResourceAsStream("aaaadb.json")!!
            val jsonResource = aaaadbFile.bufferedReader().use { it.readText() }
            jsonObject = JSONObject(jsonResource)
        }
        assertNotNull(jsonObject)
    }

    /**
      * This test verifies that all parent tags are present on the json.
      */
    @Test
    fun mandatoryFields() {
        val jsonSoundsArray = jsonObject!!.getJSONArray("sounds")
        assertNotNull(jsonSoundsArray)
        val jsonTagsArray = jsonObject!!.getJSONArray("tags")
        assertNotNull(jsonTagsArray)
    }

    /**
     * This test verifies that all mandatory fields are present for each sound of the json.
     */
    @Test
    fun soundsMandatoryFields() {
        val jsonSoundsArray = jsonObject!!.getJSONArray("sounds")
        for (i in 0 until jsonSoundsArray.length()) {
            assertNotNull((jsonSoundsArray[i] as JSONObject).getString("name"))
            assertNotNull((jsonSoundsArray[i] as JSONObject).getString("displaytext"))
            assertNotNull((jsonSoundsArray[i] as JSONObject).getString("transcript"))
        }
    }

    /**
     * This test verifies that the mp3 file referenced by the json exists in the raw folder.
     */
    @Test
    fun verifyMP3Exists() {
        val jsonSoundsArray = jsonObject!!.getJSONArray("sounds")
        for (i in 0 until jsonSoundsArray.length()) {
            val filename = (jsonSoundsArray[i] as JSONObject).getString("name")
            assertNotNull(javaClass.classLoader?.getResource("$filename.mp3"))
        }
    }

    /**
     * This test verifies that the tags of a sound are present in the tag array of the json.
     */
    @Test
    fun verifyTags() {
        val jsonTagsArray =jsonObject!!.getJSONArray("tags")
        val tags = ArrayList<String>()
        for (i in 0 until jsonTagsArray.length()) {
            assertTrue(jsonTagsArray[i] is String)
            tags.add(jsonTagsArray[i] as String)
        }

        val jsonSoundsArray = jsonObject!!.getJSONArray("sounds")
        for (i in 0 until jsonSoundsArray.length()) {
            try {
                val soundTags = (jsonSoundsArray[i] as JSONObject).getJSONArray("tags")
                for (j in 0 until soundTags.length()) {
                    assertTrue((soundTags[j] as String) in tags)
                }
            } catch (e: org.json.JSONException) {
                // There is no tags for this sound (no problem)
            }
        }
    }

    /**
     * This test verifies that each sound is only there once.
     */
    @Test
    fun verifyOnlyOneSound() {
        val jsonSoundsArray = jsonObject!!.getJSONArray("sounds")
        for (i in 0 until jsonSoundsArray.length()) {
            for (j in i+1 until jsonSoundsArray.length()) {
                if (i != j) {
                    assertNotEquals(
                        (jsonSoundsArray[i] as JSONObject).getString("name"),
                        (jsonSoundsArray[j] as JSONObject).getString("name")
                    )
                }
            }
        }
    }
}