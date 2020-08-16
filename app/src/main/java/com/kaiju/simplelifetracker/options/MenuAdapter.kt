package com.kaiju.simplelifetracker.options

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private val itemList: List<MenuView>, private val activity: AppCompatActivity) {

    class MenuViewHolder(itemView: View, private var activity: AppCompatActivity
        ): RecyclerView.ViewHolder(itemView) {

        fun bind(menuView: MenuView) {

        }

    }
}