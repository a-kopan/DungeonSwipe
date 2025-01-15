package com.example.dungeonswipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme

@Composable
fun Card(modifier: Modifier = Modifier, entity: Unit, image: Int = R.drawable.ic_money) {
    Box(modifier = Modifier
        .size(100.dp, 200.dp)
        .background(
            color = Color.Gray,
            shape = RoundedCornerShape(16.dp)
        )
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            ImageContainer(
                modifier,
                image
            )
            ValueBarContainer(
                modifier.fillMaxWidth(),
                10,
                painterResource(R.drawable.ic_money)
            )
        }
    }
}

@Composable
fun ImageContainer(modifier: Modifier = Modifier, image: Int) {
    Image(
        painter = painterResource(image),
        contentDescription = "Main Image of the card",
        modifier = Modifier
            .size(100.dp, 175.dp)
    )
}

@Composable
fun ValueBarContainer(modifier: Modifier = Modifier, value: Int, image: Painter) {
    val value = remember { mutableIntStateOf(value) }
    Row(
        horizontalArrangement = Arrangement.Center) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "${value.value}"
        )
        Icon(
            painter = image,
            contentDescription = "Counter for numeric value.",
            modifier = Modifier
                .size(25.dp)
        )
    }
}

@Preview
@Composable
fun PlainCardPreview() {
    DungeonSwipeTheme {
        Card(Modifier, TestEntity(8), R.drawable.lizardman)
    }
}