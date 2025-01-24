package com.example.dungeonswipe.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Card(var value: Int = 0, open val imageResId: Int, var textVal: Int = 0) {
    open var currentValue by mutableStateOf(value)

    fun getImage(): Int {
        return imageResId
    }

    fun addToCurrentValue(num: Int): Unit {
        currentValue+=num
    }
}