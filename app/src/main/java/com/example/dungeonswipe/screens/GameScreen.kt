package com.example.dungeonswipe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import com.example.dungeonswipe.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dungeonswipe.*
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.dataClasses.*
import kotlin.random.Random

@Composable
fun GameScreen(gameViewModel: GameViewModel, navController: NavHostController, hero: Hero = Hero()) {
    val heroState = remember { mutableStateOf(hero) }
    var enemies = mutableListOf<Enemy>()
    val availableBuffs = gameViewModel.purchasedBuffs.collectAsState().value

    for (buff in availableBuffs) {
        if (! heroState.value.buffs.any { it.name == buff.name } ) {
            buff.replaceTarget(heroState.value)
            heroState.value.addBuff(buff)
        }
    }

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
                                    heroState.value,
                                    enemies,
                                    gameViewModel,
                                    navController
                                )?.let { newPosition ->
                                    heroPosition.value = newPosition
                                    isPlayerTurn = false
                                    isWaitingForTurn = true
                                }
                            }
                            //Do something with Empty cards on the map
                            replaceEmptyCardsWithRandom(board)
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
            BuffCounterBar(
                Modifier
                    .fillMaxWidth(),
                heroState.value.buffs
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
    heroState: Hero,
    enemies: MutableList<Enemy>,
    gameViewModel: GameViewModel,
    navController: NavHostController
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

        if (heroState.currentValue<=0) {
            gameViewModel.addGold(heroState.currentMoney)
            navController.popBackStack()
        }

    }
    for (buff in heroState.buffs) {
        buff.timer-=1
        if (buff.timer<=0) {
            if (buff.specialEffectOnHero != null) {
                buff.specialEffectOnHero?.let { it(heroState) }
            } else if (buff.specialEffectOnEnemies != null) {
                //Get all enemies from the board
                for (row in board) {
                    for (card in row) {
                        if (card is Enemy) {
                            enemies.add(card)
                        }
                    }
                }
                buff.specialEffectOnEnemies?.let { it(enemies) }
                enemies.clear()
            }
            buff.timer = buff.max_timer
        }
    }


    return newPosition
}

@Composable
fun BuffCounterBar(modifier: Modifier = Modifier, buffs: List<Buff>) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (buff in buffs) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = buff.timer.toString(),
                color = buff.textColor,
                fontFamily = pixelFontFamily,
                fontSize = 35.sp
            )
        }
    }
}

fun replaceEmptyCardsWithRandom(grid: List<MutableList<Card>>) {
    for (row in grid) {
        for (col in row.indices) {
            if (row[col] is Empty) {
                row[col] = getRandomCard()  // Replace Empty card with a random card
            }
        }
    }
}

fun getRandomCard(): Card {
    val randomValue = Random.nextInt(0, 100)  // Generate a random number between 0 and 100

    return when {
        randomValue < 10 -> Weapon()
        randomValue < 20 -> Potion()
        randomValue < 30 -> Enemy()
        else -> Empty
    }
}