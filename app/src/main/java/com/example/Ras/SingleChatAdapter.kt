package com.example.Ras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.Utils.UID
import com.example.Ras.Utils.asTime
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.message_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<User>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blockUser: ConstraintLayout = view.block_user_message
        val chatUser: TextView = view.userMessage
        val chatUserTime: TextView = view.userTimeMessage

        val blockRecUser: ConstraintLayout = view.block_receiving_message
        val chatRecUser: TextView = view.receivingMessage
        val chatRecUserTime: TextView = view.receivingTimeMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].Group == UID) {
            holder.blockUser.visibility = View.VISIBLE
            holder.blockRecUser.visibility = View.GONE
            holder.chatUser.text = mListMessagesCache[position].Text
            holder.chatUserTime.text = mListMessagesCache[position].TimeStamp.toString().asTime()
        } else {
            holder.blockUser.visibility = View.GONE
            holder.blockRecUser.visibility = View.VISIBLE
            holder.chatRecUser.text = mListMessagesCache[position].Text
            holder.chatRecUserTime.text = mListMessagesCache[position].TimeStamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    fun setList(list: List<User>) {
        mListMessagesCache = list
    }
}