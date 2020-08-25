package com.kaiju.simplelifetracker.Dice

import kotlin.random.Random

class Die(num_sides: Int) {

    val sides: Int = num_sides

    fun roll(): Int = Random.nextInt(1, sides)

}