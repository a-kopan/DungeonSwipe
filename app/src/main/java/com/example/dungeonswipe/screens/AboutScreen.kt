package com.example.dungeonswipe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.R
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme

@Composable
fun AboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        TopBar(
            modifier = Modifier
                //.padding(15.dp)
                .background(Color.Blue),
            navController = navController)
        AboutText(
            modifier = Modifier.fillMaxWidth()
            .padding(top = 50.dp)
        )
        TextBlock(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.about_paragraph)
        )
    }
}

@Composable
fun AboutText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = Color.Green),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TitleFormat(text = "About")
    }
}

@Composable
fun TextBlock(modifier : Modifier = Modifier, text : String) {
    Text(
        modifier = modifier
            .padding(vertical = 20.dp),
        text = text,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun AboutScreenPreview() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "about_screen"
    ) {
        composable("main_menu") { MainMenu(navController) }
        //composable("levels_screen") { GameScreen(navController) }
        composable("shop_screen") { ShopScreen(navController) }
        composable("about_screen") { AboutScreen(navController) }
    }
    DungeonSwipeTheme {
        AboutScreen(navController)
    }
}