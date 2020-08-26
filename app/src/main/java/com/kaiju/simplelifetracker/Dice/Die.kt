package com.kaiju.simplelifetracker.Dice

import android.view.View
import android.widget.TextView
import com.kaiju.simplelifetracker.R
import kotlinx.android.synthetic.main.die_roll_dialog_die_roll.view.*
import kotlin.random.Random

class Die(num_sides: Int) {

    private val sides: Int = num_sides

    private fun roll(): String = Random.nextInt(1, sides).toString()

    fun performRollInDialog(view: View) {

        val resultTextView = view.findViewById<TextView>(R.id.layout_die_roll_result)

        resultTextView.text = this.roll()

    }

}