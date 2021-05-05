package com.example.Ras.UI.messUI

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.RegisterFragment
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment() : BaseFragment(R.layout.fragment_auth) {

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
        if (auth_Login.text.toString().isEmpty()
                || auth_Pass.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.auth_eEmpty_text), Toast.LENGTH_SHORT).show()
        } else {
            activityQ = if (actId == 1) {
                MessengerActivity()
            } else {
                MissingActivity()
            }
            authUser()
        }
    }

    private fun authUser() {
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
