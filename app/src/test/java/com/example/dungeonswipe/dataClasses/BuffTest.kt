package com.example.dungeonswipe.dataClasses

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class BuffTest {

    @Test
    fun `Healing buff should heal hero`() {
        // Given
        val hero = Hero()
        val healingBuff = buffHealing(hero)
        hero.buffs = listOf(healingBuff)
        hero.addToCurrentValue(-4)

        // When
        healingBuff.specialEffectOnHero?.let { it(hero) }

        // Then
        assertEquals(5, hero.currentValue)
    }

    @Test
    fun `Weapon buff should add armor`() {
        // Given
        val hero = Hero()
        val weaponBuff = buffWeapon(hero)
        hero.buffs = listOf(weaponBuff)

        // When
        weaponBuff.specialEffectOnHero?.let { it(hero) }

        // Then
        assertEquals(1, hero.currentWeapon.armor)
    }

}