package com.example.Ras.UI.messUI

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.*
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private lateinit var mLogin: String
    private lateinit var mPass: String
    private lateinit var activityQ: AppCompatActivity
    override fun onStart() {
        super.onStart()
        auth_action_btx.setOnClickListener {
            checkData()
        }
    }

    private fun checkData() {
        when {
            auth_Login.text.toString().isEmpty() -> {
                createToast(getString(R.string.auth_eEmpty_text))
            }
            auth_Login.text.toString().replace(Regex("[+]"), "")
                .isDigitsOnly() -> {
                authUserWithPhone()
            }
            auth_Pass.text.toString().isNotEmpty() -> {
                activityQ = if (actId == 1) {
                    MessengerActivity()
                } else {
                    MissingActivity()
                }
                authUserWithEmail()
            }
        }
    }

    private fun authUserWithPhone() {
        mLogin = auth_Login.text.toString()

        REF_DATABASE.child(NODE_PHONES).child(mLogin)
            .addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                if (snapshot.exists()) {
                    Log.d("MyLog", "authUserWithPhone: yes")
                } else {
                    Log.d("MyLog", "authUserWithPhone: no")
                }
            })
    }

    private fun authUserWithEmail() {
        mLogin = auth_Login.text.toString()
        mPass = auth_Pass.text.toString()
        AUTH.signInWithEmailAndPassword(mLogin, mPass).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                createToast(getString(R.string.welcome))
                (activity as AuthorizationActivity).replaceActivity(activityQ)
            } else createToast("Boom ${task.exception?.message.toString()}")
        }
    }
}
