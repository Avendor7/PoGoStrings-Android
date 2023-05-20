package ca.avendor.pogostrings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import ca.avendor.pogostrings.PrefDataKeyValueStore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import java.util.prefs.Preferences


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storage")

class PrefDataKeyValueStore (val context: Context){
    private object PreferenceKeys {
        val stringList = stringPreferencesKey("stringList")
    }

   suspend fun updateStringList(pogoStringList: String) = context.dataStore.edit { preferences: MutablePreferences ->
    preferences[stringList]= pogoStringList
   }

    fun readStringList(): Flow<String> = context.dataStore.data.map{ preferences: Preferences ->
        return@map preferences[stringList]  ?: false
    }
}