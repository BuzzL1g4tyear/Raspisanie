package com.example.Ras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.Utils.MESS_ACTIVITY
import com.example.Ras.Utils.createToast
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.phone_item.view.*

class AddPhoneAdapter:RecyclerView.Adapter<AddPhoneAdapter.AddPhoneHolder>() {

    private var listItems = mutableListOf<User>()

    class AddPhoneHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemPhone: TextView = view.add_auth_phone
        val itemChoice: ImageView = view.check_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPhoneHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.phone_item, parent, false)

        val holder = AddPhoneHolder(view)
        holder.itemView.setOnClickListener {
            if (listItems[holder.adapterPosition].choice){
                holder.itemChoice.visibility = View.INVISIBLE
                listItems[holder.adapterPosition].choice = false
                AddPhoneFragment.listItems.remove(listItems[holder.adapterPosition])
            } else {
                holder.itemChoice.visibility = View.VISIBLE
                listItems[holder.adapterPosition].choice = true
                AddPhoneFragment.listItems.add(listItems[holder.adapterPosition])
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AddPhoneHolder, position: Int) {
        holder.itemPhone.text = listItems[position].Phone
    }

    override fun getItemCount() = listItems.size

    fun updateListItems(item:User){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}