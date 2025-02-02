package com.example.dungeonswipe.dataClasses

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.dungeonswipe.GameViewModel
import com.example.dungeonswipe.screens.moveHero
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class EnemyTest {

    fun assertEnemyEqualsIgnoreStartingHealth(expected: Enemy, actual: Enemy) {
        assertEquals(expected.health, actual.health, "Health does not match")
        assertEquals(expected.imageResId, actual.imageResId, "Image does not match")
        assertEquals(expected.onDeathEffect, actual.onDeathEffect, "On death effect does not match")
        assertEquals(expected.perTurnEffect, actual.perTurnEffect, "Per turn effect does not match")
    }

    private fun createBoard(): SnapshotStateList<SnapshotStateList<Card>> {
        return SnapshotStateList<SnapshotStateList<Card>>().apply {
            repeat(3) {
                add(SnapshotStateList<Card>().apply {
                    repeat(3) { add(Empty) }
                })
            }
        }
    }

    private val gameViewModel: GameViewModel = mockk(relaxed = true)
    private val navController: NavHostController = mockk(relaxed = true)
    private val enemies = mutableListOf<Enemy>()
    private val board = createBoard()

    @Test
    fun `Zombie should spawn skeleton on death`() {
        // Given
        val hero = Hero()
        val zombie = zombie()
        val expected = skeleton()
        board[1][1] = hero
        board[1][2] = zombie

        // When
        moveHero(
            board,
            Pair(1,1),
            "RIGHT",
            hero,
            enemies,
            gameViewModel,
            navController
        )
        val enemy: Enemy = board[1][2] as Enemy

        // Then
        assertEnemyEqualsIgnoreStartingHealth(enemy, expected)
    }
}