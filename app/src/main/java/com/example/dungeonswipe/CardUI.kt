package com.example.dungeonswipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dungeonswipe.dataClasses.*
import com.example.dungeonswipe.ui.theme.DungeonSwipeTheme

@Composable
fun CardUI(modifier: Modifier = Modifier, CardDataClass: Card) {
    val color : Color
    if (CardDataClass is Hero) {
        color = Color.Yellow
    } else {
        color = Color.Black
    }
    val param = when (CardDataClass) {
        is Enemy -> CardDataClass.health
        is Weapon -> CardDataClass.armor
        is Potion -> CardDataClass.heal
        else -> CardDataClass.currentValue
    }

    //UI
    Box(modifier = modifier
        .size(100.dp, 200.dp)
        .background(
            color = Color.Gray,
            shape = RoundedCornerShape(16.dp)
        )
        .fillMaxSize(0.5f)
        .border(
            BorderStroke(2.dp, color),
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            ImageContainer(
                Modifier,
                CardDataClass.getImage()
            )
            if (CardDataClass is Hero) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                )
                {
                    ValueBarContainer(
                        Modifier,
                        CardDataClass.currentValue,
                        painterResource(R.drawable.hearth)
                    )
                    ValueBarContainer(
                        Modifier,
                        CardDataClass.currentWeapon.armor,
                        painterResource(R.drawable.ic_sword),
                        imageSize = 20.dp,
                        imageTopPadding = 5.dp
                    )
                }
            } else if (CardDataClass !is Empty) {
                ValueBarContainer(
                    Modifier.fillMaxWidth(),
                    param,
                    painterResource(R.drawable.hearth)
                )
            } else {}
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
fun ValueBarContainer(modifier: Modifier = Modifier, value: Int, image: Painter, imageSize: Dp = 25.dp, imageTopPadding: Dp = 0.dp) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "${value}"
        )
        Icon(
            painter = image,
            contentDescription = "Counter for numeric value.",
            modifier = Modifier
                .size(imageSize)
                .padding(top = imageTopPadding)
        )
    }
}

@Preview
@Composable
fun PlainCardPreview() {
    DungeonSwipeTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            val mod = Modifier.padding(horizontal = 5.dp)
            CardUI(mod, Hero())
            CardUI(mod, CardDataClass = Enemy())
            //CardUI(mod, CardDataClass = Empty)
            CardUI(mod, CardDataClass = Weapon())
            //CardUI(mod, CardDataClass = Potion())
        }
    }
}