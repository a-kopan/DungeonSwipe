package com.example.dungeonswipe.dataClasses

import com.example.dungeonswipe.R
import kotlin.math.floor

data class Enemy(
    var health: Int = 4,
    val startingHealth: Int = health,
    var onDeathEffect: (() -> Unit)? = null,
    val perTurnEffect: (() -> Unit)? = null,
    override var imageResId: Int = R.drawable.zombie_copy
) : Card(value = health, imageResId = imageResId)

fun becomeSkeleton(enemy: Enemy) {
    enemy.health = floor(enemy.startingHealth.toDouble()/2).toInt()
    enemy.imageResId = R.drawable.skeleton
    enemy.onDeathEffect = null
}


//Monster declarations
fun zombie(health: Int = 4): Enemy {
    var zombie = Enemy(
        health = health,
        imageResId = R.drawable.zombie_copy
    )
    zombie.onDeathEffect = { becomeSkeleton(zombie) }
    return zombie
}

fun skeleton(health: Int = 2): Enemy {
    return Enemy(
        health = health,
        imageResId = R.drawable.skeleton
    )
}