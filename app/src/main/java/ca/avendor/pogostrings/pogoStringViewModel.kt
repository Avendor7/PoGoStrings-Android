package ca.avendor.pogostrings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class pogoStringViewModel(

    private val dao: PoGoStringsDao


): ViewModel() {


    private val _pogoStringFlow= dao.getPogoStrings()
    private val _state = MutableStateFlow(PoGoStringsState())
    val state = combine(_pogoStringFlow, _state) { pogoStrings, state ->
        state.copy(
            pogoStrings = pogoStrings,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        PoGoStringsState()
    )
    fun onEvent(event: PoGoStringsEvent) {
        when (event) {
            is PoGoStringsEvent.DeletePoGoString -> {
                viewModelScope.launch{
                    dao.deletePoGoString(event.pogoString)
                }
            }
            PoGoStringsEvent.SavePoGoString -> {

                val pogoStringItem = state.value.pogoStringItem

                if (pogoStringItem.isBlank()){
                    return
                }
                val pogoString = PoGoString(
                    pogoStringItem = pogoStringItem
                )

                viewModelScope.launch {
                    dao.upsertPoGoString(pogoString)
                }
                _state.update { it.copy(
                    pogoStringItem = ""
                )}

            }

            is PoGoStringsEvent.SetPoGoStringItem -> {
                _state.update {it.copy(
                    pogoStringItem = event.pogoStringItem
                    )
                }
            }
        }
    }



    private var stringList = mutableStateListOf(
        PoGoString(0, "My First Task"),
        PoGoString(1, "My Second Task"),
        PoGoString(2, "My Third Task"),

    )


    //private val _pogoStringFlow = MutableStateFlow<List<PoGoString>>(getStringList())

    val pogoStringFlow get() = _pogoStringFlow



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
//    suspend fun addToStringList(stringitem: String) {
//
//        val gson = Gson()
//        val mutableStringList = stringList
//
//        val regularList = mutableStringList.toList()
//        val jsonString = gson.toJson(regularList)
//
//
//        datasource.edit {
//            it[stringPreferencesKey("pogoStringList")] = jsonString
//        }
//    }
//
//
//    suspend fun getStringList(): SnapshotStateList<PoGoString> {
//        //TODO replace shared preferences with datastore
//        //https://developer.android.com/jetpack/androidx/releases/datastore
//        //https://developer.android.com/topic/libraries/architecture/datastore#additional-resources
//        //
//        val dataStoreFlow: Flow<Any> = datasource.data
//            .map { preferences ->
//                // No type safety.
//                preferences[stringPreferencesKey("pogoStringList")] ?: 0
//            }
//            .catch { exception ->
//                // dataStore.data throws an IOException when an error is encountered when reading data
//                // No type safety.
//                if (exception is IOException) {
//                    emit(0)
//                } else {
//                    throw exception
//                }
//            }
//
//        val value = dataStoreFlow.first()
//
//        val gson = Gson()
//        val json = value
//
//        if(json == null)
//            stringList = gson.fromJson("[]", object : TypeToken<ArrayList<PoGoString>>() {}.type)
//        else
//            stringList = gson.fromJson(json.toString(), object : TypeToken<ArrayList<PoGoString>>() {}.type)
//
//        return stringList
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