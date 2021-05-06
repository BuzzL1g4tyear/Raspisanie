package com.example.Ras.UI.messUI

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.Ras.ChangeNameFragment
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.RegisterFragment
import com.example.Ras.Utils.MESS_ACTIVITY
import com.example.Ras.Utils.USER
import com.example.Ras.Utils.initDatabase
import com.example.Ras.Utils.replaceFragment
import kotlinx.android.synthetic.main.activity_messenger.*

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var status: String = ""

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.messenger)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        status = USER.Status
        Log.d("MyLog", "chatfr: $status")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when {
            status.equals("4", true) -> {
                (activity as MessengerActivity).menuInflater.inflate(R.menu.items_admin, menu)
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
        }
        return true
    }
}