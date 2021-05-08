package com.example.Ras.UI.messUI

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.Ras.AuthorizationActivity
import com.example.Ras.EnterCodeFragment
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.concurrent.TimeUnit

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private lateinit var mLogin: String
    private lateinit var mPass: String
    private lateinit var activityQ: AppCompatActivity
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                AUTH.signInWithCredential(p0).addOnCompleteListener() {
                    if (it.isSuccessful) {
                        createToast("welcome")
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                createToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mLogin, id))
            }
        }

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

        val query: Query = REF_DATABASE.child(NODE_PHONES).child(mLogin)
        query.addListenerForSingleValueEvent(AppValueEventListener {
            if (it.key == mLogin) {
                val options = PhoneAuthOptions.newBuilder(AUTH)
                    .setPhoneNumber(mLogin)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(AUTH_ACTIVITY)                 // Activity (for callback binding)
                    .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } else {
                Log.d("MyLog", "authUserWithPhone: no")
            }
        })
    }

    private fun authUserWithEmail() {
        mLogin = auth_Login.text.toString()
        mPass = auth_Pass.text.toString()
        AUTH.signInWithEmailAndPassword(mLogin, mPass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createToast(getString(R.string.welcome))
                (activity as AuthorizationActivity).replaceActivity(activityQ)
            } else createToast("Boom ${task.exception?.message.toString()}")
        }
    }
}
