package com.example.dungeonswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.dataClasses.Buff
import com.example.dungeonswipe.dataClasses.Hero
import com.example.dungeonswipe.dataClasses.buffHealing
import com.example.dungeonswipe.dataClasses.buffWeapon
import com.example.dungeonswipe.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Create a repository instance
        val repository = GameRepository(applicationContext)

        // Create a custom ViewModelFactory for the repository
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(repository) as T
            }
        }

        setContent {
            DungeonSwipeTheme {
                val gameViewModel: GameViewModel = viewModel( factory = factory)
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main_menu"
                ) {
                    composable("main_menu") { MainMenu(navController, gameViewModel) }
                    composable("game_screen") {
                        var hero = Hero()
                        GameScreen(gameViewModel, navController, hero = hero)
                    }
                    composable("shop_screen") { ShopScreen(navController, gameViewModel) }
                    composable("about_screen") { AboutScreen(navController) }
                }
            }
        }
    }

}


