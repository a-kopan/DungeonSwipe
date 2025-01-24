package com.example.dungeonswipe.dataClasses

import com.example.dungeonswipe.R

data class Weapon(
    var armor: Int = 5,
    override val imageResId: Int = R.drawable.sword
) : Card(value = armor, imageResId = imageResId)