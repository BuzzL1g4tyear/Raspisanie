package com.example.Ras

import androidx.fragment.app.Fragment
import com.example.Ras.Utils.AUTH
import com.example.Ras.Utils.AppTextWatcher
import com.example.Ras.Utils.MESS_ACTIVITY
import com.example.Ras.Utils.createToast
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val mLogin: String, val id: String) : Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()
        MESS_ACTIVITY.title = mLogin
        enterCode.addTextChangedListener(AppTextWatcher {
            val str = enterCode.text.toString()
            if (str.length == 6) {
                verificationCode()
            }
        })
    }

    private fun verificationCode() {
        val code = enterCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                createToast("welcome")
            }
        }
    }

}