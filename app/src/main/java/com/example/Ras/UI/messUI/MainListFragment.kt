package com.example.Ras.UI.messUI

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.Ras.*
import com.example.Ras.Utils.*
import com.example.Ras.models.User

class MainListFragment : Fragment(R.layout.fragment_chat) {

    private val mRefMainList = REF_DATABASE
        .child(NODE_MAIN_LIST)
        .child(UID)
    private val mRefGroups = REF_DATABASE
        .child(NODE_GROUP_CHAT)
    private var mList = listOf<User>()

    private var status: String = ""
    private var numberGroup: String = ""

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.messenger)
        setHasOptionsMenu(true)
        AddPhoneFragment.listItems.clear()
        initMainChat()
    }

    private fun initMainChat() {
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { Data ->
            mList = Data.children.map { it.getUserModel() }

            mList.forEach { model ->
                mRefGroups.child(model.id).addListenerForSingleValueEvent(AppValueEventListener {
                    val newModel = it.getUserModel()
                    replaceFragment(GroupChatFragment(newModel), false)
                })
            }
        })
    }

    override fun onStart() {
        super.onStart()
        status = USER.Status
        numberGroup = USER.Group
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when {
            status.equals("4", true) -> {
                MESS_ACTIVITY
                    .menuInflater.inflate(R.menu.items_admin, menu)
            }
            status.equals("3", true) -> {
                MESS_ACTIVITY
                    .menuInflater.inflate(R.menu.items_curator, menu)
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
}