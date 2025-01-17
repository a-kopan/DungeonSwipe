package com.example.dungeonswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DungeonSwipeTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main_menu"
                ) {
                    composable("main_menu") { MainMenu(navController) }
                    composable("game_screen") { GameScreen(navController) }
                    composable("shop_screen") { ShopScreen(navController) }
                    composable("about_screen") { AboutScreen(navController) }
                }
            }
        }
    }

}


