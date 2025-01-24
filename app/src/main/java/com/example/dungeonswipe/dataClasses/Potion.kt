package com.example.dungeonswipe.dataClasses

import com.example.dungeonswipe.R

data class Potion(
    val heal: Int = 5,
    override val imageResId: Int = R.drawable.potion
) : Card(value = heal, imageResId = imageResId) {
    fun healHero(hero: Hero) {
        hero.addToCurrentValue(heal)
        if (hero.currentValue>hero.startingHealth) {
            hero.currentValue = hero.startingHealth
        }
    }
}