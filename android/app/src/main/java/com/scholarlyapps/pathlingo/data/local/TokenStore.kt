package com.scholarlyapps.pathlingo.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("pathlingo_auth")

class TokenStore(private val context: Context) {

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }

    suspend fun token(): String? = tokenFlow.first()

    suspend fun save(token: String) {
        context.dataStore.edit { it[KEY_TOKEN] = token }
    }

    suspend fun clear() {
        context.dataStore.edit { it.remove(KEY_TOKEN) }
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("sanctum_token")
    }
}
