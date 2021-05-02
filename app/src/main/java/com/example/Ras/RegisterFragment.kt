package com.example.Ras

import android.util.Log
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment() : BaseFragment(R.layout.fragment_register) {
    override fun onStart() {
        super.onStart()
        button.setOnClickListener {
            m()
        }
    }

    private fun m() {
        val a: String = ed1.text.toString()
        val b: String = ed2.text.toString()
        val s: String = ed3.text.toString()
        AUTH.createUserWithEmailAndPassword(a, b)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val uId = AUTH.currentUser?.uid.toString()
                        val dataMap = mutableMapOf<String, Any>()
                        dataMap[CHILD_ID] = uId
                        dataMap[CHILD_EMAIL] = a
                        dataMap[CHILD_FULLNAME] = a
                        dataMap[CHILD_STATUS] = s
                        REF_DATABASE.child(NODE_USERS).child(uId).updateChildren(dataMap)
                    } else {
                        Log.d("MyLog", "Boom ${task.exception?.message.toString()} ")
                    }
                }
    }
}