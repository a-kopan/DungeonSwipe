package com.example.dungeonswipe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import com.example.dungeonswipe.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.dataClasses.*
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme

@Composable
fun GameScreen(navController: NavHostController, hero: Hero = Hero()) {
    val heroState = remember { mutableStateOf(hero) }
    val board = remember {
        mutableStateListOf(
            mutableStateListOf<Card>(Weapon(2), Weapon(5), Weapon()),
            mutableStateListOf<Card>(zombie(), Hero(), zombie()),
            mutableStateListOf<Card>(skeleton(), Potion(), Enemy())
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
                                moveHero(
                                    board,
                                    heroPosition.value,
                                    direction,
                                    heroState.value
                                )?.let { newPosition ->
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
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                navController = navController,
                amountOfCash = heroState.value.currentMoney
            )
            GameBoard(
                modifier = Modifier
                    .fillMaxSize(),
                board = board,
                heroPosition = heroPosition.value,
                heroState = heroState.value
            )
        }
    }

}

@Composable
fun GameBoard(modifier: Modifier = Modifier, board: List<List<Card>>, heroPosition: Pair<Int, Int>, heroState: Hero) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (rowIndex in board.indices) {
            Row {
                for (colIndex in board[rowIndex].indices) {
                    val card = board[rowIndex][colIndex]
                    if (rowIndex == heroPosition.first && colIndex == heroPosition.second) {
                        CardUI(modifier = Modifier.padding(8.dp), CardDataClass = heroState)
                    } else {
                        CardUI(modifier = Modifier.padding(8.dp), CardDataClass = card)
                    }
                }
            }
        }
    }
}

fun moveHero(
    board: SnapshotStateList<SnapshotStateList<Card>>,
    heroPosition: Pair<Int, Int>,
    direction: String,
    heroState: Hero
): Pair<Int, Int>? {
    val (row, col) = heroPosition
    var newPosition = when (direction) {
        "UP" -> if (row > 0) Pair(row - 1, col) else null
        "DOWN" -> if (row < board.size - 1) Pair(row + 1, col) else null
        "LEFT" -> if (col > 0) Pair(row, col - 1) else null
        "RIGHT" -> if (col < board[0].size - 1) Pair(row, col + 1) else null
        else -> null
    }

    newPosition?.let { (newRow, newCol) ->
        val targetTile = board[newRow][newCol]

        when (targetTile) {
            is Enemy -> {
                heroState.attackEnemy(targetTile)
                if (targetTile.health==0) {
                    //If enemy has no onDeathEffect then just replace him with empty cell
                    if (targetTile.onDeathEffect == null) {
                        board[newRow][newCol] = Empty
                    } else {
                        targetTile.onDeathEffect?.let { it() }
                    }

                }
                newPosition = Pair(row,col)
            }
            is Weapon -> {
                heroState.equipWeapon(targetTile)
                board[row][col] = Empty
                board[newRow][newCol] = heroState
            }
            is Potion -> {
                targetTile.healHero(heroState)
                board[row][col] = Empty
                board[newRow][newCol] = heroState
            }
            is Hero -> {}
            is Empty -> {
                board[row][col] = Empty
                board[newRow][newCol] = heroState
            }
            else -> {}
        }

    }

    return newPosition
}


@Preview
@Composable
fun PlainCardPreview() {
    DungeonSwipeTheme {
        val navController = rememberNavController()

        GameScreen(navController)
    }
}