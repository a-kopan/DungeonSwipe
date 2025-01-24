package com.example.dungeonswipe.dataClasses

import androidx.compose.ui.graphics.Color

class Buff(
    val name: String = "Healing",
    val max_timer: Int = 3,
    var timer: Int = max_timer,
    var specialEffectOnHero: ((hero: Hero) -> Unit)? = null,
    val specialEffectOnEnemies: ((enemies: List<Enemy>) -> Unit)? = null,
    val textColor: Color = Color.Black,
    val buffPrice: Int = 4,
    val buffDescription: String = "",
    var target: Hero
) {
    fun replaceTarget(newTarget: Hero) {
        this.target = newTarget
    }
}

//Buffs
fun buffHealing(hero: Hero): Buff {
    var buff = Buff(
        name = "Healing",
        max_timer = 7,
        textColor = Color.Red,
        buffPrice = 2,
        buffDescription = "After the counter reaches 0, heal the hero with 2 hp!",
        target = hero
    )
    buff.specialEffectOnHero = {healHero(hero = buff.target) }
    return buff
}

fun buffWeapon(hero: Hero): Buff {
    var buff = Buff(
        name = "Healing",
        max_timer = 7,
        textColor = Color.Black,
        buffPrice = 2,
        buffDescription = "After the counter reaches 0, heal the hero with 2 hp!",
        target = hero
    )
    buff.specialEffectOnHero = { upgradeWeapon(hero = buff.target) }
    return buff
}


//Special effects
fun healHero(hero: Hero) {
    val temp_potion = Potion(heal = 2)
    temp_potion.healHero(hero)
}


fun upgradeWeapon(hero: Hero) {
    hero.currentWeapon.armor+=1
}