package com.example.Ras

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.UI.messUI.GroupChatFragment
import com.example.Ras.UI.messUI.MainListFragment
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_add_phone.*

class AddPhoneFragment(private var numberGroup: String) :
    Fragment(R.layout.fragment_add_phone) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddPhoneAdapter

    private val mRefAuthPhone = REF_DATABASE.child(NODE_AUTHPHONES).child(numberGroup)

    private var mListItems = listOf<User>()

    override fun onResume() {
        super.onResume()
        MESS_ACTIVITY.title = getString(R.string.createGroupTitle)
        MESS_ACTIVITY.mAppDrawer.enableDrawer()

        initRecyclerView()
        create_group_auth_phone.setOnClickListener {
            createGroup(numberGroup, listItems) {
                replaceFragment(MainListFragment())
            }
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_auth_phone_rec_view
        mAdapter = AddPhoneAdapter()

        mRefAuthPhone.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getUserModel() }
            mListItems.forEach { model ->
                mAdapter.updateListItems(model)

            }
        })
        mRecyclerView.adapter = mAdapter
    }

    companion object {
        val listItems = arrayListOf<User>()
    }
}