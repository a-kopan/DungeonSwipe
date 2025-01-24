package com.example.dungeonswipe

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.dungeonswipe.dataClasses.Buff
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _goldCount = MutableStateFlow(0)
    val goldCount: StateFlow<Int> = _goldCount.asStateFlow()

    private val _purchasedBuffs = MutableStateFlow<List<Buff>>(emptyList())
    val purchasedBuffs: StateFlow<List<Buff>> = _purchasedBuffs.asStateFlow()

    init {
        // Load the initial gold count from the repository
        viewModelScope.launch {
            repository.goldCountFlow.collect { savedGoldCount ->
                _goldCount.value = savedGoldCount
            }
            repository.getPurchasedBuffsFlow().collect { savedBuffs ->
                _purchasedBuffs.value = savedBuffs
            }
        }
    }

    fun buyBuff(buff: Buff) {
        // Check if user has enough gold
        if (_goldCount.value >= buff.buffPrice) {
            // Deduct the gold
            addGold(-buff.buffPrice)

            // Add the buff to the list of purchased buffs
            _purchasedBuffs.value = _purchasedBuffs.value + buff

            // Save the updated list of purchased buffs to the repository
            saveBuffsToRepository()
        }
    }

    // Function to add gold
    fun addGold(amount: Int) {
        _goldCount.value += amount
        saveGoldToRepository() // Save the updated gold count
    }


    // Save the gold count to the repository
    private fun saveGoldToRepository() {
        viewModelScope.launch {
            repository.saveGoldCount(_goldCount.value)
        }
    }
    // Save the purchased buffs to the repository
    private fun saveBuffsToRepository() {
        viewModelScope.launch {
            repository.savePurchasedBuffs(_purchasedBuffs.value)
        }
    }
}