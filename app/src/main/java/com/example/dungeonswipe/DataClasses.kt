package com.example.dungeonswipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Card(var value: Int = 0, open val imageResId: Int, var textVal: Int = 0) {
    var currentValue by mutableStateOf(value)

    fun getImage(): Int {
        return imageResId
    }

    fun fetchCurrentValue(): Int {
        return currentValue
    }

    fun addToCurrentValue(num: Int): Unit {
        currentValue+=num
    }
}

// Card subclasses
object Hero : Card(value = 7, imageResId = R.drawable.hero)

data class Enemy(
    val health: Int = 2,
    val onDeathEffect: (() -> Unit)? = null,
    val perTurnEffect: (() -> Unit)? = null,
    override val imageResId: Int = R.drawable.zombie_copy
) : Card(value = health, imageResId = imageResId) {
    fun attackHero(hero: Card) {
        hero.addToCurrentValue((-1)*health)
    }
}

data class Weapon(
    val armor: Int = 5,
    override val imageResId: Int = R.drawable.skeleton
) : Card(value = armor, imageResId = imageResId)

data class Potion(
    val heal: Int = 5,
    override val imageResId: Int = R.drawable.lizardman
) : Card(value = heal, imageResId = imageResId)

object Empty: Card(value = 0, imageResId = R.drawable.ic_money)

fun isPositive(card: Card): Boolean {
   if (card is Enemy) {
       return false
   } else {
       return true
   }
}

class SpecialEffect

class Buff(private val timer: Int, private val specialEffect: SpecialEffect)
