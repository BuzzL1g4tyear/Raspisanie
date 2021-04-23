package com.example.Ras

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.example.Ras.Utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {

    override fun onStart() {
        super.onStart()
        (activity as MessengerActivity).mAppDrawer.disableDrawer()
        setHasOptionsMenu(true)
    }

    override fun onStop() {
        super.onStop()
        (activity as MessengerActivity).mAppDrawer.enableDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MessengerActivity).menuInflater.inflate(R.menu.item_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.confirm_change_name -> {
                changeName()
            }
        }
        return true
    }

    private fun changeName() {
        val name = change_name.text.toString()
        val surname = change_surname.text.toString()
        if (name.isEmpty()) {
            createToast(getString(R.string.change_eEmpty_Name_text))
        } else {
            val fullName = "$name $surname"
            REF_DATABASE.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
                    .setValue(fullName).addOnCompleteListener {
                        if (it.isSuccessful) {
                            createToast(getString(R.string.changedData))
                            USER.fullname = fullName
                            fragmentManager?.popBackStack()
                        }
                    }
        }
    }
}