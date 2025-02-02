import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.dungeonswipe.GameViewModel
import com.example.dungeonswipe.dataClasses.*
import com.example.dungeonswipe.screens.moveHero
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class HeroMovementTest {

    // Helper to create a board
    private fun createBoard(): SnapshotStateList<SnapshotStateList<Card>> {
        return SnapshotStateList<SnapshotStateList<Card>>().apply {
            repeat(3) {
                add(SnapshotStateList<Card>().apply {
                    repeat(3) { add(Empty) }
                })
            }
        }
    }

    private fun refreshBoard(board: SnapshotStateList<SnapshotStateList<Card>>): Unit {
        for (row in 0..2) {
            for (column in 0..2) {
                board[row][column] = Empty
            }
        }
    }

    private val gameViewModel: GameViewModel = mockk(relaxed = true)
    private val navController: NavHostController = mockk(relaxed = true)
    private val enemies = mutableListOf<Enemy>()
    private val board = createBoard()

    @Test
    fun `Hero should be able to do legal moves`() {
        // Given
        val hero = Hero()
        refreshBoard(board)

        val directions = listOf("UP" to Pair(0,1), "DOWN" to Pair(2,1), "LEFT" to Pair(1,0), "RIGHT" to Pair(1,2))

        // When
        directions.forEach { (direction, expectedPos) ->
            val result = moveHero(
                board = board,
                heroPosition = Pair(1, 1),
                direction = direction,
                heroState = hero,
                enemies = enemies,
                gameViewModel = gameViewModel,
                navController = navController
            )

            // Then
            assertEquals(expectedPos, result, "FIRST ASSERTION $direction")
            assertNotEquals(hero, board[1][1], "SECOND ASSERTION")
            assertEquals(hero, board[expectedPos.first][expectedPos.second], "THIRD ASSERTION")

            refreshBoard(board)
            board[1][1] = hero
        }
    }

    @Test
    fun `Hero shouldn't be able to do illegal moves`() {
        val hero = Hero()
        board[0][0] = hero

        val directions = listOf("UP", "LEFT")

        directions.forEach { direction ->
            val result = moveHero(
                board = board,
                heroPosition = Pair(0, 0),
                direction = direction,
                heroState = hero,
                enemies = enemies,
                gameViewModel = gameViewModel,
                navController = navController
            )

            assertNull(result)
            assertEquals(hero, board[0][0])
        }
    }

    @Test
    fun `Hero shouldn't move after attacking an enemy`() {
        // Given
        refreshBoard(board)
        val hero = Hero(currentValue = 10)
        val enemy = Enemy(health = 3)
        board[1][1] = hero
        board[1][2] = enemy

        // When
        val result = moveHero(
            board = board,
            heroPosition = Pair(1, 1),
            direction = "RIGHT",
            heroState = hero,
            enemies = enemies,
            gameViewModel = gameViewModel,
            navController = navController
        )

        // Then
        assertEquals(Pair(1,1), result)
        assertEquals(0, enemy.health)
        assertEquals(Empty, board[1][2])
    }

    @Test
    fun `Hero's buffs timers should decrease after every move`() {
        refreshBoard(board)
        val hero = Hero()
        val buff = Buff(max_timer = 2, target = hero)
        hero.addBuff(buff)

        // Initial move (timer decrements to 0)
        moveHero(board, Pair(1,1), "UP", hero, enemies, gameViewModel, navController)
        assertEquals(1, buff.timer)

        // Next move (timer resets to max_timer)
        moveHero(board, Pair(0,1), "DOWN", hero, enemies, gameViewModel, navController)
        assertEquals(2, buff.timer)
    }
}