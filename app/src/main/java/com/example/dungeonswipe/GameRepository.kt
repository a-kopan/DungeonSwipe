package com.example.dungeonswipe

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.dungeonswipe.dataClasses.Buff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class GameRepository(private val context: Context) {

    // Define a key for storing gold count
    private val GOLD_COUNT_KEY = intPreferencesKey("gold_count")
    private val _purchasedBuffsFlow = MutableStateFlow<List<Buff>>(emptyList())

    // Get the gold count as a Flow
    val goldCountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[GOLD_COUNT_KEY] ?: 0 // Default value is 0
    }

    fun getPurchasedBuffsFlow(): StateFlow<List<Buff>> = _purchasedBuffsFlow
    fun savePurchasedBuffs(buffList: List<Buff>) {
        _purchasedBuffsFlow.value = buffList
    }

    // Function to save gold count
    suspend fun saveGoldCount(goldCount: Int) {
        context.dataStore.edit { preferences ->
            preferences[GOLD_COUNT_KEY] = goldCount
        }
    }
}