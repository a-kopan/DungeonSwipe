package com.example.dungeonswipe

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameEffect() {}

@Composable
fun TestEntity(value: Int, image: Painter = painterResource(R.drawable.ic_money)) {}

@Composable
fun SpecialEffect(privateCounter: Int, effect: @Composable() () -> Unit) {}

@Composable
fun Hero(health: Int, image: Painter, specialEffect: @Composable() () -> Unit) {}

@Composable
fun Weapon(damage: Int, image: Painter, range: Int) {}

@Composable
fun Potion(heal: Int) {}

@Composable
fun Money(value: Int) {}

@Composable
fun Enemy(health: Int, image: Painter, specialEffect: @Composable() () -> Unit) {}

