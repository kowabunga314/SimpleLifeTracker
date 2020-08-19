package com.kaiju.simplelifetracker.options

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val itemList: List<MenuView>,
    private val activity: AppCompatActivity
):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View, private var activity: AppCompatActivity
    ): RecyclerView.ViewHolder(itemView) {

//        private val optionKey = itemView.option_key

        fun bind(menuView: MenuView) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = itemList[position]

        when (holder) {

            is MenuViewHolder -> {
                holder.bind(currentItem)
            }
        }
    }
}