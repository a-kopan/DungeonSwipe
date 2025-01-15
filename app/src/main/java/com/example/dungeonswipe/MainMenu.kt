package com.example.dungeonswipe

import android.view.Menu
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme

@Composable
fun MainMenu() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        //Bar at the top with money
        MoneyBar()

        //Main title text
        TitleText()

        //Column with buttons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuButton("Levels")
            MenuButton("Shop")
            MenuButton("About")
            MenuButton("Exit")
        }

        //Version at the bottom
        VersionText()
    }
}

@Composable
fun MoneyBar(amountOfCash: Int = 0) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_money),
            contentDescription = "Money amount icon"
        )
        Text(text = amountOfCash.toString())
    }
}

@Composable
fun TitleText() {
    Column(
        modifier = Modifier
            .background(color = Color.Green)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Dungeon")
        Text("Swipe")
    }
}

@Composable
fun MenuButton(name: String) {
    ElevatedButton(
        modifier = Modifier
            .background(color = Color.Yellow)
            .padding(vertical = 20.dp),
        onClick = {}
    ) {
        Text(
            text = name)
    }
}

@Composable
fun VersionText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(vertical = 10.dp),
        text = "Version: 0.0.1"
    )
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    DungeonSwipeTheme {
        MainMenu()
    }
}