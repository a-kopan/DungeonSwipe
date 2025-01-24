package com.example.dungeonswipe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dungeonswipe.GameViewModel
import com.example.dungeonswipe.R
import com.example.dungeonswipe.pixelFontFamily
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme
import com.example.dungeonswipe.screens.*

@Composable
fun MainMenu(navController: NavHostController, gameViewModel: GameViewModel) {
    val context = LocalContext.current
    val goldCount by gameViewModel.goldCount.collectAsState(initial = 0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        //Bar at the top with money
        TopBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            navController = navController,
            amountOfCash = goldCount
        )

        //Main title text
        TitleText(
            modifier =Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 50.dp))

        //Column with buttons
        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
                .fillMaxWidth()
                .padding(top = 140.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuButton("Play", navController = navController, destination = "game_screen")
            MenuButton("Shop", navController = navController, destination = "shop_screen")
            MenuButton("About", navController = navController, destination = "about_screen")
            MenuButton("Exit", navController = navController, destination = "main_menu",onExit = {(context as? android.app.Activity)?.finish()})
        }

        //Version at the bottom
        VersionText(
            modifier =Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 5.dp),
            version = "0.0.1")
    }
}

@Composable
fun HomeButton(modifier: Modifier = Modifier,
               navController: NavHostController) {
    IconButton(
        onClick = {
            navController.navigate("main_menu") {
                popUpTo("main_menu") { inclusive = true }
            }
        },
        modifier = Modifier
            .size(20.dp)
    ) {
        // Use a home emote or any custom image as an icon
        Image(
            painter = painterResource(id = R.drawable.ic_home),
            modifier = Modifier.size(20.dp),
            contentDescription = "Home Button"
        )
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, amountOfCash: Int = 0, navController: NavHostController) {
    Row(
        modifier = modifier
            .padding(top = 15.dp, end = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_money),
            modifier = Modifier.size(40.dp),
            contentDescription = "Money amount icon"
        )
        Text(
            text = amountOfCash.toString(),
            style = TextStyle (
                fontSize = 28.sp,
                color = Color(0xFFFFD700)
            )
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            HomeButton(
                modifier = Modifier.padding(horizontal = 10.dp),
                navController = navController)
        }
    }
}

@Composable
fun TitleFormat(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        fontFamily = pixelFontFamily,
        fontSize = 60.sp,
        text = text,
        style = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(2f,2f),
                blurRadius = 1f
            )
        ),
    )
}

@Composable
fun TitleText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TitleFormat(text = "Dungeon")
        TitleFormat(text = "Swipe")
    }
}

@Composable
fun MenuButton(
    name: String,
    navController: NavHostController,
    destination: String,
    onExit: (() -> Unit)? = null
    ) {
    ElevatedButton(
        modifier = Modifier
            .padding(vertical = 30.dp)
            .size(width = 240.dp, height = 90.dp)
            .fillMaxWidth(),
        onClick = {
            if (onExit!=null){
                onExit()
            }
            navController.navigate(destination)
        }
    ) {
        Text(
            text = name,
            fontFamily = pixelFontFamily,
            fontSize = 20.sp
        )
    }
}

@Composable
fun VersionText(modifier: Modifier = Modifier, version: String) {
    Text(
        modifier = modifier
            .padding(vertical = 10.dp),
        style = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(2f,2f),
                blurRadius = 1f
            )
        ),
        fontFamily = pixelFontFamily,
        text = "version: "+version
    )
}
//
//@Preview
//@Composable
//fun MainMenuPreview() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "main_menu"
//    ) {
//        composable("main_menu") { MainMenu(navController) }
//        //composable("game_screen") { GameScreen(navController) }
//        composable("shop_screen") { ShopScreen(navController) }
//        composable("about_screen") { AboutScreen(navController) }
//    }
//    DungeonSwipeTheme {
//        MainMenu(navController)
//    }
//}