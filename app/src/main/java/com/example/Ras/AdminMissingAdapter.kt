package com.example.Ras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.Utils.asTime
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.miss_person_item.view.*

class AdminMissingAdapter : RecyclerView.Adapter<AdminMissingAdapter.AdminMissingHolder>() {

    private var listItems = mutableListOf<User>()

    class AdminMissingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemGroup: TextView = view.group_item
        val itemSurname: TextView = view.surname_item
        val itemCause: TextView = view.cause_item
        val itemTime: TextView = view.time_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminMissingHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.miss_person_item, parent, false)

        return AdminMissingHolder(view)
    }

    override fun onBindViewHolder(holder: AdminMissingHolder, position: Int) {
            holder.itemSurname.text = listItems[position].Surname //0.
            holder.itemGroup.text = listItems[position].Group
            holder.itemTime.text = listItems[position].TimeStamp.toString().asTime()
            holder.itemCause.text = listItems[position].Cause
    }

    override fun getItemCount() = listItems.size

    fun updateListItems(item: User) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}