package com.example.Ras.UI.messUI

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.*
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment() : BaseFragment(R.layout.fragment_auth) {

    private lateinit var mLogin: String
    private lateinit var mPass: String
    private lateinit var activityQ: AppCompatActivity
    override fun onStart() {
        super.onStart()
        auth_action_btx.setOnClickListener(View.OnClickListener {
            checkData()
        })
    }

    private fun checkData() {
        if (auth_Login.text.toString().isEmpty()
                || auth_Pass.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.auth_eEmpty_text), Toast.LENGTH_SHORT).show()
        } else {
            if (actId == 1) {
                activityQ = MessengerActivity()
            } else {
                activityQ = MissingActivity()
            }
            authUser()
        }
    }

    private fun authUser() {
        mLogin = auth_Login.text.toString()
        mPass = auth_Pass.text.toString()

        AUTH.signInWithEmailAndPassword(mLogin, mPass).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val uId = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uId
                dataMap[CHILD_EMAIL] = mLogin
                dataMap[CHILD_USERNAME] = uId
                REF_DATABASE.child(NODE_USERS).child(uId).updateChildren(dataMap)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                createToast("ok")
                                (activity as AuthorizationActivity).replaceActivity(activityQ)
                            } else createToast("Boom ${task2.exception?.message.toString()}")
                        }
                fragmentManager?.beginTransaction()?.addToBackStack(null)
                        ?.replace(R.id.authData_container, ChatFragment())
                        ?.commit()
            }
        }
    }
}