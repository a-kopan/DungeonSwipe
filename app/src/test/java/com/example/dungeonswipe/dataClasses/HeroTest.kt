package com.example.dungeonswipe.dataClasses
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.example.dungeonswipe.dataClasses.Hero
import com.example.dungeonswipe.dataClasses.Enemy
import com.example.dungeonswipe.dataClasses.Weapon
import com.example.dungeonswipe.dataClasses.Buff

class HeroTest {

    @Test
    fun testEquipWeapon_HigherArmor() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(10))  // Hero starts with a weapon of armor 10
        val newWeapon = Weapon(20)  // New weapon has higher armor

        // Act
        hero.equipWeapon(newWeapon)

        // Assert
        assertEquals(newWeapon, hero.currentWeapon, "Weapon should be updated to the new weapon with higher armor")
    }

    @Test
    fun testEquipWeapon_LowerArmor() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(10))  // Hero starts with a weapon of armor 10
        val newWeapon = Weapon(5)  // New weapon has lower armor

        // Act
        hero.equipWeapon(newWeapon)

        // Assert
        assertEquals(Weapon(10), hero.currentWeapon, "Weapon should not be updated because the new weapon has lower armor")
    }

    @Test
    fun testAddBuff() {
        // Setup
        val hero = Hero()
        val buff = Buff(name = "Health Boost", buffDescription = "Increases health", buffPrice = 10, target = hero)

        // Act
        hero.addBuff(buff)

        // Assert
        assertTrue(hero.buffs.contains(buff), "Buff should be added to the hero's list of buffs")
    }

    @Test
    fun testAttackEnemy_WithWeapon() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(10))  // Hero starts with a weapon with armor 10
        val enemy = Enemy(health = 50)  // Enemy has 50 health

        // Act
        hero.attackEnemy(enemy)

        // Assert
        assertEquals(40, enemy.health, "Enemy's health should be reduced by the weapon's armor (50 - 10 = 40)")
        assertEquals(0, hero.currentWeapon.armor, "Weapon's armor should be depleted after the attack")
    }

    @Test
    fun testAttackEnemy_WithoutWeapon() {
        // Setup
        val hero = Hero(currentValue = 10)  // Hero without a weapon, attack power = 10
        val enemy = Enemy(health = 50)  // Enemy has 50 health

        // Act
        hero.attackEnemy(enemy)

        // Assert
        assertEquals(40, enemy.health, "Enemy's health should be reduced by the hero's current value (50 - 10 = 40)")
        assertEquals(0, hero.currentValue, "Hero's current value should be depleted after the attack")
    }

    @Test
    fun testAttackEnemy_KillEnemy() {
        // Setup
        val hero = Hero(currentValue = 10)  // Hero without a weapon, attack power = 10
        val enemy = Enemy(health = 10)  // Enemy has 10 health

        // Act
        hero.attackEnemy(enemy)

        // Assert
        assertEquals(0, enemy.health, "Enemy's health should be 0 after the attack")
        assertEquals(10, hero.currentMoney, "Hero should receive money equal to the enemy's starting health")
    }

    @Test
    fun testAttackEnemy_WithWeapon_KillEnemy() {
        // Setup
        val hero = Hero(currentWeapon = Weapon(15))  // Hero starts with a weapon with armor 15
        val enemy = Enemy(health = 20)  // Enemy has 20 health

        // Act
        hero.attackEnemy(enemy)

        // Assert
        assertEquals(0, enemy.health, "Enemy's health should be 0 after the attack")
        assertEquals(5, hero.currentWeapon.armor, "Weapon's armor should be reduced to 5 after killing the enemy (15 - 20 = -5, weapon's armor should carry the leftover damage)")
    }
}
