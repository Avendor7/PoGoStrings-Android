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


    val pogoStringFlow get() = _pogoStringFlow

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

}