package com.example.smartforks.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data")
        private val ALLERGY_KEY = stringPreferencesKey("allergy_key")
        private val DIET_KEY = stringPreferencesKey("diet_key")
        private val HEIGHT_KEY = stringPreferencesKey("height_key")
        private val WEIGHT_KEY = stringPreferencesKey("weight_key")
        private val GOAL_KEY = stringPreferencesKey("goal_key")
    }

    val getAllergy: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ALLERGY_KEY] ?: ""
    }

    suspend fun saveAllergy(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ALLERGY_KEY] = token
        }
    }

    val getDiet: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DIET_KEY] ?: ""
    }

    suspend fun saveDiet(token: String) {
        context.dataStore.edit { preferences ->
            preferences[DIET_KEY] = token
        }
    }

    val getHeight: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[HEIGHT_KEY] ?: ""
    }

    suspend fun saveHeight(token: String) {
        context.dataStore.edit { preferences ->
            preferences[HEIGHT_KEY] = token
        }
    }

    val getWeight: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WEIGHT_KEY] ?: ""
    }

    suspend fun saveWeight(token: String) {
        context.dataStore.edit { preferences ->
            preferences[WEIGHT_KEY] = token
        }
    }

    val getGoal: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[GOAL_KEY] ?: ""
    }

    suspend fun saveGoal(token: String) {
        context.dataStore.edit { preferences ->
            preferences[GOAL_KEY] = token
        }
    }

}