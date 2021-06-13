package com.example.Ras.UI.messUI

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.*
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_messenger.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_chat_info.view.*

class GroupChatFragment(private val group: User) : Fragment(R.layout.fragment_single_chat) {

    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMainList: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: GroupChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 10
    private var status: String = ""
    private var numberGroup: String = ""
    private var messID = ""
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private var mListListeners = mutableListOf<AppChildEventListener>()

    override fun onStart() {
        super.onStart()
        status = USER.Status
        numberGroup = USER.Group
        mRefMainList = REF_DATABASE
            .child(NODE_MAIN_LIST)
            .child(UID)
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { list ->
            messID = list.children.map {
                it.getUserModel().id
            }.toString()
            messID = messID.replace("[", "").replace("]", "")
        })
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = chat_rv
        mAdapter = GroupChatAdapter()

        mRefMessages = REF_DATABASE
            .child(NODE_GROUP_CHAT)
            .child(group.id)
            .child(NODE_MESSAGES)

        mRecyclerView.adapter = mAdapter
        mMessagesListener = AppChildEventListener {
            mAdapter.addItem(it.getUserModel(), mSmoothScrollToPosition)
            if (mSmoothScrollToPosition) {
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }
        })
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when {
            status.equals("3", true) -> {
                MESS_ACTIVITY
                    .menuInflater.inflate(R.menu.items_curator, menu)
            }
            status.equals("", true) -> {

            }
            else -> MESS_ACTIVITY
                .menuInflater.inflate(R.menu.items_messenger_toolbar, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
            R.id.createUser -> {
                replaceFragment(RegisterFragment())
            }
            R.id.addUser -> {
                MESS_ACTIVITY.phonePick()
            }
            R.id.addGroup -> {
                replaceFragment(AddPhoneFragment(numberGroup))
            }
            R.id.updUser -> {
                replaceFragment(UpdUserFragment())
            }
        }
        return true
    }

    private fun initToolbar() {
        mToolbarInfo = MESS_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mRefUser = REF_DATABASE.child(NODE_USERS).child(group.id)
        chat_send_message.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = message_chat.text.toString()
            if (message.isNotEmpty()) {
                sendGroupMessage(message, group.id, TYPE_TEXT) {
                    message_chat.setText("")
                }
            }
        }

        mToolbarInfo.chat_info_group.text = group.FullName
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mListListeners.forEach {
            mRefMessages.removeEventListener(it)
        }
    }
}