package com.example.Ras;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.Utils.DiffUtilCallback
import com.example.Ras.Utils.UID
import com.example.Ras.Utils.asTime
import com.example.Ras.models.User
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.message_item.view.*

class GroupChatAdapter : RecyclerView.Adapter<GroupChatAdapter.singleChatHolder>() {

    private var mListMessCache = mutableListOf<User>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): singleChatHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return singleChatHolder(view)
    }

    override fun onBindViewHolder(holder: singleChatHolder, position: Int) {
        if (mListMessCache[position].From == UID) {
            holder.block_user_message.visibility = View.VISIBLE
            holder.block_receiving_message.visibility = View.GONE

            holder.chat_user_message.text = mListMessCache[position].Text
            holder.chat_user_message_time.text =
                mListMessCache[position].TimeStamp.toString().asTime()
        } else {
            holder.block_user_message.visibility = View.GONE
            holder.block_receiving_message.visibility = View.VISIBLE

            holder.chat_receiving_message.text = mListMessCache[position].Text
            holder.chat_receiving_message_time.text =
                mListMessCache[position].TimeStamp.toString().asTime()
        }
    }

    override fun getItemCount() = mListMessCache.size

    fun addItem(
        item: User,
        toBottom: Boolean,
        onSuccess: () -> Unit
    ) {
        if (toBottom) {
            if (!mListMessCache.contains(item)) {
                mListMessCache.add(item)
                notifyItemInserted(mListMessCache.size)
            }
        } else {
            if (!mListMessCache.contains(item)) {
                mListMessCache.add(item)
                mListMessCache.sortBy { it.TimeStamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSuccess()
    }

    class singleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val block_user_message: ConstraintLayout = view.block_user_message
        val chat_user_message: TextView = view.userMessage
        val chat_user_message_time: TextView = view.userTimeMessage

        val block_receiving_message: ConstraintLayout = view.block_receiving_message
        val chat_receiving_message: TextView = view.receivingMessage
        val chat_receiving_message_time: TextView = view.receivingTimeMessage
    }
}
