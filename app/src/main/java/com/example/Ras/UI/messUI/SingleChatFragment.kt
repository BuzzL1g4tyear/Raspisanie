package com.example.Ras.UI.messUI

import android.view.View
import com.example.Ras.R
import com.example.Ras.Utils.AppValueEventListener
import com.example.Ras.Utils.MESS_ACTIVITY
import com.example.Ras.Utils.createToast
import com.example.Ras.Utils.getUserModel
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.android.synthetic.main.activity_messenger.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_chat_info.*
import kotlinx.android.synthetic.main.toolbar_chat_info.view.*

class SingleChatFragment : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mToolbarInfo: View

    override fun onResume() {
        super.onResume()

        mToolbarInfo = MESS_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE

        mListenerInfoToolbar = AppValueEventListener{
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
    }

    private fun initInfoToolbar() {
        mToolbarInfo.chat_info_group
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
    }
}