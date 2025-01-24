package com.example.dungeonswipe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.GameViewModel
import com.example.dungeonswipe.R
import com.example.dungeonswipe.dataClasses.Buff
import com.example.dungeonswipe.dataClasses.Hero
import com.example.dungeonswipe.dataClasses.buffHealing
import com.example.dungeonswipe.dataClasses.buffWeapon
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme


@Composable
fun ShopScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val goldCount by gameViewModel.goldCount.collectAsState(initial = 0)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.shop),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        TopBar(
            modifier = Modifier
                .align(Alignment.TopStart),
            navController = navController,
            amountOfCash = goldCount,
        )

        ShopTab(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 400.dp),
            buff = buffHealing(Hero()),
            gameViewModel = gameViewModel
        )

        ShopTab(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 100.dp),
            buff = buffWeapon(Hero()),
            gameViewModel = gameViewModel
        )
    }
}

@Composable
fun ShopTab(modifier: Modifier = Modifier, buff: Buff = buffHealing(Hero()), gameViewModel: GameViewModel) {
    var isBought by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .background(Color.Gray)
            .height(120.dp)
    ) {
        //Price Block (and buy button/bought unclickable)
        Column(
            modifier = Modifier
                .weight(0.2f),
        ) {
            ElevatedButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (!isBought) {
                        // Item is bought, disable the button and update the state

                        //implement adding buff to available buffs
                        gameViewModel.buyBuff(buff)
                        if (gameViewModel.goldCount.value >= buff.buffPrice) {
                            isBought = true
                        }
                    }
                },
                enabled = !isBought // Disable the button when bought
            ) {
                Text(
                    text = buff.buffPrice.toString(),
                    fontSize = 40.sp
                )
            }
        }
        //Description
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray),
                text = buff.buffDescription,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f,2f),
                        blurRadius = 1f
                    )
                )
            )
        }
    }
}