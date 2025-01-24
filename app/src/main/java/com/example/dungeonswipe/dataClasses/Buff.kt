package com.example.dungeonswipe.dataClasses

class Buff(
    val max_timer: Int = 3,
    var timer: Int = max_timer,
    val specialEffectOnHero: ((hero: Hero) -> Unit)? = null,
    val specialEffectOnEnemies: ((enemies: List<Enemy>) -> Unit)? = null
) {

}

//Special effects
fun healHero(hero: Hero) {
    val temp_potion = Potion(heal = 2)
    temp_potion.healHero(hero)
}

fun clearDungeon(enemies: List<Enemy>){

}
