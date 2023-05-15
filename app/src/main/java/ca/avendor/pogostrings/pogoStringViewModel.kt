package ca.avendor.pogostrings

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class pogoStringViewModel: ViewModel() {


    private var stringList = mutableStateListOf(
        PoGoString(0, "My First Task"),
        PoGoString(1, "My Second Task"),
        PoGoString(2, "My Third Task"),

    )

    //private val _pogoStringFlow = MutableStateFlow<List<PoGoString>>(getStringList())
    private val _pogoStringFlow = MutableStateFlow<List<PoGoString>>(stringList)

    val pogoStringFlow: StateFlow<List<PoGoString>> get() = _pogoStringFlow

    //val pogoStringList: ArrayList<PoGoString> = ArrayList<PoGoString>()

    fun addString(newString: String) {
        if (stringList.isEmpty())
            stringList.add(PoGoString(0, newString))
        else
            stringList.add(PoGoString(stringList.last().id +1, newString))
    }
    fun removeString(stringItem: PoGoString){
        val index = stringList.indexOf(stringItem)
        stringList.remove(stringList[index])
    }
//
//    private fun addToStringList(stringitem: String) {
//
//        //val pogoStringList: ArrayList<PoGoString> = ArrayList<PoGoString>()
//        // val pogoStringList = getStringList()
//        if(pogoStringList.isEmpty()) {
//            pogoStringList.add(PoGoString(0, stringitem))
//        }else {
//            pogoStringList.add(PoGoString(pogoStringList.last().id + 1, stringitem))
//        }
//        saveData()
//    }
//    private fun getStringList(): ArrayList<PoGoString> {
//        //TODO replace shared preferences with datastore
//        //https://developer.android.com/jetpack/androidx/releases/datastore
//        //
//        val sharedPreferences = getSharedPreferences("shared preferences",
//            AppCompatActivity.MODE_PRIVATE
//        )
//        val gson = Gson()
//        val json = sharedPreferences.getString("strings", "[]")
//
//        if(json == null)
//            pogoStringList = ArrayList()
//        else
//            pogoStringList = gson.fromJson(json, object : TypeToken<ArrayList<PoGoString>>() {}.type)
//
//        return pogoStringList
//    }
//
//    //pogoStringList.add(PoGoString(stringitem))
//
//
//    //save to shared preferences. Converts to Json in order to save
//    private fun saveData() {
//
//        val sharedPreferences = getSharedPreferences("shared preferences",
//            AppCompatActivity.MODE_PRIVATE
//        )
//        val editor = sharedPreferences.edit()
//        val gson = Gson()
//        val json = gson.toJson(pogoStringList)
//        editor.putString("strings", json)
//        editor.apply()
//
//    }

}