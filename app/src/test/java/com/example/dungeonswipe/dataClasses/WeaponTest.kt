package com.example.dungeonswipe.dataClasses

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeaponTest {
    @Test
    fun `Weapon should be updated to a weapon with higher value`() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(10))
        val newWeapon = Weapon(20)

        // Act
        hero.equipWeapon(newWeapon)

        // Assert
        assertEquals(newWeapon, hero.currentWeapon, "Weapon should be updated to the new weapon with higher armor")
    }

    @Test
    fun `Weapon shouldn't update for lower value`() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(10))  // Hero starts with a weapon of armor 10
        val newWeapon = Weapon(5)  // New weapon has lower armor

        // Act
        hero.equipWeapon(newWeapon)

        // Assert
        assertEquals(Weapon(10), hero.currentWeapon, "Weapon should not be updated because the new weapon has lower armor")
    }
}