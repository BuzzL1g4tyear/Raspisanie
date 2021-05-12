package com.example.Ras

import android.util.Log
import android.widget.ArrayAdapter
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment() : BaseFragment(R.layout.fragment_register) {
    override fun onStart() {
        super.onStart()
        setSpinner()
        (activity as MessengerActivity).title = getString(R.string.addNewUser)
        button.setOnClickListener {
            creteUserWithEmail()
        }
    }

    private fun setSpinner() {
        val status = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_status, status)
        status_spinner.setAdapter(adapter)
    }

    // TODO: 06.05.2021 красивый вью, group
    private fun creteUserWithEmail() {
        val email: String = ed1.text.toString()
        val password: String = ed2.text.toString()
        val status: String = ed3.text.toString()
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val uId = AUTH.currentUser?.uid.toString()
                    val dataMap = mutableMapOf<String, Any>()
                    dataMap[CHILD_ID] = uId
                    dataMap[CHILD_EMAIL] = email
                    dataMap[CHILD_FULLNAME] = email
                    dataMap[CHILD_STATUS] = status
                    REF_DATABASE.child(NODE_USERS).child(uId).updateChildren(dataMap)
                } else {
                    Log.d("MyLog", "Boom ${task.exception?.message.toString()} ")
                }
            }
    }
}