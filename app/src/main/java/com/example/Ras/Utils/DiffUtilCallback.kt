package com.example.Ras.Utils

import androidx.recyclerview.widget.DiffUtil
import com.example.Ras.models.User

class DiffUtilCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].TimeStamp == newList[newItemPosition].TimeStamp

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}