package com.example.dungeonswipe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import com.example.dungeonswipe.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dungeonswipe.*
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.ui.res.painterResource

@Composable
fun GameScreen(navController: NavHostController) {
    val board = remember {
        mutableStateListOf(
            mutableStateListOf<Card>(Empty, Empty, Weapon()),
            mutableStateListOf<Card>(Empty, Hero, Enemy()),
            mutableStateListOf<Card>(Empty, Potion(), Enemy())
        )
    }

    val heroPosition = remember { mutableStateOf(Pair(1, 1)) }
    var isPlayerTurn by remember { mutableStateOf(true) }
    var isWaitingForTurn by remember { mutableStateOf(false) }

    LaunchedEffect(isWaitingForTurn) {
        if (isWaitingForTurn) {
            delay(500)
            isPlayerTurn = true
            isWaitingForTurn = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        if (isPlayerTurn && !isWaitingForTurn) {
                            val (dx, dy) = dragAmount
                            val direction = when {
                                dy < 0 && dx.absoluteValue < dy.absoluteValue -> "UP"
                                dy > 0 && dx.absoluteValue < dy.absoluteValue -> "DOWN"
                                dx < 0 && dy.absoluteValue < dx.absoluteValue -> "LEFT"
                                dx > 0 && dy.absoluteValue < dx.absoluteValue -> "RIGHT"
                                else -> null
                            }

                            direction?.let {
                                moveHero(board, heroPosition.value, direction)?.let { newPosition ->
                                    heroPosition.value = newPosition
                                    isPlayerTurn = false
                                    isWaitingForTurn = true
                                }
                            }
                        }
                    }
                },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GameBoard(board = board, heroPosition = heroPosition.value)
        }
    }

}

@Composable
fun GameBoard(board: List<List<Card>>, heroPosition: Pair<Int, Int>) {
    Column(
        modifier = Modifier.padding(top = 100.dp)
    ) {
        for (rowIndex in board.indices) {
            Row {
                for (colIndex in board[rowIndex].indices) {
                    val card = board[rowIndex][colIndex]
                    if (rowIndex == heroPosition.first && colIndex == heroPosition.second) {
                        CardUI(modifier = Modifier.padding(4.dp), CardDataClass = Hero)
                    } else {
                        CardUI(modifier = Modifier.padding(4.dp), CardDataClass = card)
                    }
                }
            }
        }
    }
}

fun moveHero(board: SnapshotStateList<SnapshotStateList<Card>>, heroPosition: Pair<Int, Int>, direction: String): Pair<Int, Int>? {
    val (row, col) = heroPosition
    val newPosition = when (direction) {
        "UP" -> if (row > 0) Pair(row - 1, col) else null
        "DOWN" -> if (row < board.size - 1) Pair(row + 1, col) else null
        "LEFT" -> if (col > 0) Pair(row, col - 1) else null
        "RIGHT" -> if (col < board[0].size - 1) Pair(row, col + 1) else null
        else -> null
    }

    newPosition?.let { (newRow, newCol) ->
        val targetTile = board[newRow][newCol]
        val hero = board[heroPosition.first][heroPosition.second]
        when (targetTile) {
            is Enemy -> {
                targetTile.attackHero(hero)
            }
            is Weapon -> {
                heroEquipWeapon(targetTile)
            }
            is Hero -> {}
            is Potion -> {
                heroDrinkPotion(targetTile)
            }
            else -> {
            }
        }
        board[row][col] = Empty
        board[row][col].currentValue = 0
        board[newRow][newCol] = hero
        refreshHero(board, newRow, newCol)

        if (hero.currentValue <= 0) {

        }
    }

    return newPosition
}

fun heroEquipWeapon(weapon: Weapon) {
}

fun heroDrinkPotion(potion: Potion) {
}

fun refreshHero(board: SnapshotStateList<SnapshotStateList<Card>>, row: Int, col: Int) {
    val hero = board[row][col]
    board[row].remove(hero)
    board[row].add(col, hero)
}