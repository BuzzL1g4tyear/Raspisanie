package com.example.Ras;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lesson_item.view.*

class ShowAdapter : RecyclerView.Adapter<ShowAdapter.ShowHolder>() {

    private var listItems = mutableListOf<String>()

    class ShowHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemLesson: TextView = view.lesson
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShowHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false)
        return ShowHolder(view)
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ShowHolder, position: Int) {
        holder.itemLesson.text = listItems[position]
    }

    fun updateListItems(item: String) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}
