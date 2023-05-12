package ca.avendor.pogostrings
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken

object PreferenceManager {
    private const val PREFERENCE_NAME = "MyPrefs"
    private const val POGO_STRING_LIST = "pogoStringList"

    private val gson = Gson()
    fun setStringList(context: Context, stringList: List<String>) {
        val json = gson.toJson(stringList)
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(POGO_STRING_LIST, json)
        editor.apply()
    }

    fun getStringList(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(POGO_STRING_LIST, null)
        return if (json != null) {
            gson.fromJson(json, Array<String>::class.java).toList()
        } else {
            emptyList()
        }
    }

}