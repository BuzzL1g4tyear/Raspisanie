package com.example.Ras.UI.messUI

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.Ras.ChangeNameFragment
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.Utils.replaceFragment
import kotlinx.android.synthetic.main.activity_messenger.*

class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.messenger)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MessengerActivity).menuInflater.inflate(R.menu.items_messenger_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
        }
        return true
    }
}