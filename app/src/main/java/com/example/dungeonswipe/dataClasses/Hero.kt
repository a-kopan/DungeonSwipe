package com.example.dungeonswipe.dataClasses

import com.example.dungeonswipe.R

data class Hero(
    override val imageResId: Int = R.drawable.hero,
    override var currentValue: Int = 7,
    val startingHealth: Int = currentValue,
    var currentWeapon: Weapon = Weapon(0),
    var currentMoney: Int = 0,
    var buffs: List<Buff> = listOf()
) : Card(value = currentValue, imageResId = imageResId) {
    fun equipWeapon(weapon: Weapon) {
        if (weapon.armor > currentWeapon.armor) {
            currentWeapon = weapon
        }
    }

    fun addBuff(buff: Buff) {
        this.buffs += buff
    }

    fun attackEnemy(
        enemy: Enemy,
    ) {
        val hasWeapon = this.currentWeapon.armor>0
        if (hasWeapon) {
            val diff = enemy.health - this.currentWeapon.armor
            if (diff>0) {
                enemy.health = diff
                this.currentWeapon.armor = 0
            } else if (diff < 0) {
                enemy.health = 0
                this.currentWeapon.armor = (-1)*diff
            } else {
                enemy.health = 0
                this.currentWeapon.armor = 0
            }

            // If weapon wasn't enough to kill
            if (currentWeapon.armor<=0) {
                currentWeapon.armor=0
            }
        } else {
            val diff = enemy.health - this.currentValue
            if (diff>0) {
                enemy.health = diff
                this.currentValue = 0
            } else if (diff < 0) {
                enemy.health = 0
                this.currentValue = (-1)*diff
            } else {
                enemy.health = 0
                this.currentValue = 0
            }

        }
        if (enemy.health<=0) {
            enemy.onDeathEffect?.let { it() }
            this.currentMoney += enemy.startingHealth
        }
    }
}