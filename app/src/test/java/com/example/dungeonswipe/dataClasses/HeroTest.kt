package com.example.dungeonswipe.dataClasses

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class HeroTest {
    @Test
    fun `Weapon should be updated to a weapon with higher value`() {
        // Given
        val hero = Hero(currentWeapon = Weapon(10))
        val newWeapon = Weapon(20)

        // When
        hero.equipWeapon(newWeapon)

        // Then
        assertEquals(newWeapon, hero.currentWeapon, "Weapon should be updated to the new weapon with higher armor")
    }

    @Test
    fun `Weapon shouldn't update for lower value`() {
        // Given
        val hero = Hero(currentWeapon = Weapon(10))
        val newWeapon = Weapon(5)

        // When
        hero.equipWeapon(newWeapon)

        // Then
        assertEquals(Weapon(10), hero.currentWeapon, "Weapon should not be updated because the new weapon has lower armor")
    }

    @Test
    fun `Buffs should be addable`() {
        // Given
        val hero = Hero()
        val buff = Buff(name = "Health Boost", buffDescription = "Increases health", buffPrice = 10, target = hero)

        // When
        hero.addBuff(buff)

        // Then
        assertTrue(hero.buffs.contains(buff), "Buff should be added to the hero's list of buffs")
    }

    @Test
    fun `Weapon armor should empty before hero hp`() {
        // Given
        val hero = Hero(currentWeapon = Weapon(10))
        val enemy = Enemy(health = 50)

        // When
        hero.attackEnemy(enemy)

        // Then
        assertEquals(40, enemy.health, "Enemy's health should be reduced by the weapon's armor (50 - 10 = 40)")
        assertEquals(0, hero.currentWeapon.armor, "Weapon's armor should be depleted after the attack")
    }

    @Test
    fun `Hero should lose hp on attack with no weapon`() {
        // Given
        val hero = Hero(currentValue = 10)
        val enemy = Enemy(health = 50)

        // When
        hero.attackEnemy(enemy)

        // Then
        assertEquals(40, enemy.health, "Enemy's health should be reduced by the hero's current value (50 - 10 = 40)")
        assertEquals(0, hero.currentValue, "Hero's current value should be depleted after the attack")
    }

    @Test
    fun `Hero should get money after slaying a monster`() {
        // Given
        val hero = Hero(currentValue = 10)
        val enemy = Enemy(health = 10)

        // When
        hero.attackEnemy(enemy)

        // Then
        assertEquals(10, hero.currentMoney, "Hero should receive money equal to the enemy's starting health")
    }

    @Test
    fun `Hero should attack with weapon first, then with hp`() {
        // Given
        val hero = Hero(currentWeapon = Weapon(15))
        val enemy = Enemy(health = 20)

        // When 1
        hero.attackEnemy(enemy)

        // Then 1
        assertEquals(5, enemy.health, "Enemy's health should be lowered by the weapon's armor")
        assertEquals(0, hero.currentWeapon.armor, "Weapon should be destroyed after the attack")

        // When 2
        hero.attackEnemy(enemy)

        // Then 2
        assertEquals(0, enemy.health, "Enemy's health should be 0 after the final attack")
        assertEquals(2, hero.currentValue, "Hero's health should be decreased after the attack")
    }
}
