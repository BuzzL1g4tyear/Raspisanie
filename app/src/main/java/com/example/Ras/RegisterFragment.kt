package com.example.Ras

import android.util.Log
import android.widget.ArrayAdapter
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    override fun onStart() {
        super.onStart()
        setSpinner()
        MESS_ACTIVITY.title = getString(R.string.addNewUser)
        button.setOnClickListener {
            creteUserWithEmail()
        }
    }

    private fun setSpinner() {
        val statusArr = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_status, statusArr)
        status_spinner.setAdapter(adapter)
    }

    private fun creteUserWithEmail() {
        val group = ed3.text.toString()
        val email = ed1.text.toString()
        val password = ed2.text.toString()
        val status = status_spinner.text.toString()
        var statusID = ""
        when (status) {
            "Староста" -> {
                statusID = "2"
            }
            "Куратор" -> {
                statusID = "3"
            }
            "Администратор" -> {
                statusID = "4"
            }
        }

        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val uId = AUTH.currentUser?.uid.toString()
                    val dataMap = mutableMapOf<String, Any>()
                    dataMap[CHILD_ID] = uId
                    dataMap[CHILD_EMAIL] = email
                    dataMap[CHILD_FULLNAME] = email
                    dataMap[CHILD_GROUP] = group
                    dataMap[CHILD_STATUS] = statusID
                    REF_DATABASE.child(NODE_USERS).child(uId).updateChildren(dataMap)
                }
            }
    }
}