package com.example.Ras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.Utils.asTime
import com.example.Ras.models.MissingPers
import kotlinx.android.synthetic.main.miss_person_item.view.*

class AdminMissingAdapter : RecyclerView.Adapter<AdminMissingAdapter.AdminMissingHolder>() {

    private var listMissItems = mutableListOf<MissingPers>()

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
        listMissItems[position].Disease.forEach {
            holder.itemSurname.text = it //0.
            holder.itemGroup.text = listMissItems[position].Group
            holder.itemTime.text = listMissItems[position].TimeStamp.toString().asTime()
            holder.itemCause.text = "Болезнь"
        }
    }

    override fun getItemCount() = listMissItems.size

    fun updateListItems(item: MissingPers) {
        listMissItems.add(item)
        notifyItemInserted(listMissItems.size)
    }
}