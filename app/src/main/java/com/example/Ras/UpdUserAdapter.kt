package com.example.Ras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.user_item.view.*

class UpdUserAdapter : RecyclerView.Adapter<UpdUserAdapter.UpdUserHolder>() {

    private var listItems = mutableListOf<User>()

    class UpdUserHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemEmail: TextView = view.upd_user_email
        val itemStatus: TextView = view.upd_user_status
        val itemGroup: TextView = view.upd_user_group
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdUserHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)

        return UpdUserHolder(view)
    }

    override fun onBindViewHolder(holder: UpdUserHolder, position: Int) {
        holder.itemEmail.text = listItems[position].Email
        holder.itemStatus.text = (when (listItems[position].Status) {
            "2" -> "Староста"
            "3" -> "Куратор"
            else -> "Администратор"
        }).toString()
        holder.itemGroup.text = listItems[position].Group
    }

    override fun getItemCount() = listItems.size

    fun updateListItems(item: User) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    fun deleteItem(index: Int) {
        listItems.removeAt(index)
        notifyDataSetChanged()
    }
}