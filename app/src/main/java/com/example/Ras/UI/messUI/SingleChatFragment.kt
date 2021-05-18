package com.example.Ras.UI.messUI

import android.view.View
import com.example.Ras.R
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.android.synthetic.main.activity_messenger.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_chat_info.*
import kotlinx.android.synthetic.main.toolbar_chat_info.view.*

class SingleChatFragment(private val contact: User) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference

    override fun onResume() {
        super.onResume()

        mToolbarInfo = MESS_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE

        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        chat_send_message.setOnClickListener {
            val message = message_chat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    message_chat.setText("")
                }
            }
        }
    }

    private fun initInfoToolbar() {
        mToolbarInfo.chat_info_group.text = mReceivingUser.Group
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }
}