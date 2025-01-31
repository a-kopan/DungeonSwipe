package com.example.dungeonswipe.dataClasses

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PotionTest {
    @Test
    fun `Potion should heal hero for its heal value`() {
        // Given
        val potion = Potion(10)
        val hero = Hero(currentValue = 20)

        // When
        hero.addToCurrentValue(-15)
        potion.healHero(hero)

        // Then
        assertEquals(15 ,hero.currentValue)
    }

    @Test
    fun `Potion shouldn't heal for more than hero's max hp`() {
        // Given
        val potion = Potion(100)
        val hero = Hero(currentValue = 20)

        // When
        hero.addToCurrentValue(-10)
        potion.healHero(hero)

        // Then
        assertEquals(hero.startingHealth, hero.currentValue)
    }
}