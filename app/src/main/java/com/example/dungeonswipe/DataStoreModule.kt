package com.example.dungeonswipe
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Extension property for the DataStore instance
val Context.dataStore by preferencesDataStore("game_preferences")