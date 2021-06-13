package com.example.Ras

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_upd_user.*

class UpdUserFragment : BaseFragment(R.layout.fragment_upd_user) {
    private val mRefUsers = REF_DATABASE.child(NODE_USERS)
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: UpdUserAdapter
    private var mList = mutableListOf<User>()

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        MESS_ACTIVITY.title = getString(R.string.users)

    }

    private fun initRecyclerView() {
        mRecyclerView = upd_user_rv
        mAdapter = UpdUserAdapter()

        mRefUsers.addListenerForSingleValueEvent(AppValueEventListener { data ->
            mList = data.children.map { it.getUserModel() } as MutableList<User>
            mList.forEach { model ->
                mAdapter.updateListItems(model)
            }
        })
        mRecyclerView.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        initSwipe()
    }

    private fun initSwipe() {
        val swipeDelete = object : AppSwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.deleteItem(viewHolder.adapterPosition)

            }
        }
        val touchHelper = ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(mRecyclerView)
    }
}