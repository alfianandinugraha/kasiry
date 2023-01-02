package com.kasiry.app.utils.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "caches")

class LocalStore(context: Context) {
    private val dataStore = context.dataStore
    private val coroutine = CoroutineScope(Dispatchers.IO)

    suspend fun readString(key: String): String? = withContext(coroutine.coroutineContext) {
        dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }.first()
    }

    suspend fun writeString(key: String, value: String) = withContext(coroutine.coroutineContext) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun deleteString(key: String) = withContext(coroutine.coroutineContext) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
}

private var singleton: LocalStore? = null
val Context.localStore: LocalStore
    get() {
        if (singleton == null) {
            singleton = LocalStore(this)
            return singleton!!
        }

        return singleton!!
    }

private val accessTokenScope = CoroutineScope(Dispatchers.IO)
var Context.accessToken: String?
    get() {
        return runBlocking(accessTokenScope.coroutineContext)  {
            localStore.readString("access-token")
        }
    }
    set(value) {
        runBlocking(accessTokenScope.coroutineContext) {
            localStore.writeString("access-token", value ?: "")
        }
    }
