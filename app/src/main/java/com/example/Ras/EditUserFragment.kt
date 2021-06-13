package com.example.Ras

import android.widget.ArrayAdapter
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.MESS_ACTIVITY
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment(val user: User) : BaseFragment(R.layout.fragment_edit_user) {

    override fun onStart() {
        super.onStart()
        setFields()
        MESS_ACTIVITY.title = user.FullName
    }

    private fun setFields() {
        edit_user_Group.setText(user.Group)
        edit_user_email.setText(user.Email)
        var status = ""
        when (user.Status) {
            "2" -> {
                status = "Староста"
            }
            "3" -> {
                status = "Куратор"
            }
            "4" -> {
                status = "Администратор"
            }
        }
        status_spinner.setText(status)
        setSpinner()
    }

    private fun setSpinner() {
        val statusArr = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_status, statusArr)
        status_spinner.setAdapter(adapter)
    }
}