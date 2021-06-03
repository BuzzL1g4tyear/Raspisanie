package com.example.Ras

import androidx.fragment.app.Fragment
import com.example.Ras.UI.messUI.MainListFragment
import com.example.Ras.Utils.*
import com.example.Ras.models.User
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val mLogin: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()
        AUTH_ACTIVITY.title = mLogin
        enterCode.addTextChangedListener(AppTextWatcher {
            val str = enterCode.text.toString()
            if (str.length == 6) {
                verificationCode()
            }
        })
    }

    private fun verificationCode() {
        val pathUser = REF_DATABASE.child(NODE_PHONES).child(mLogin) //nbb

        var curatorID: String
        var numGroup = ""
        pathUser
            .addListenerForSingleValueEvent(AppValueEventListener {
                curatorID = it.value.toString()
                val pathCurator = REF_DATABASE.child(NODE_USERS).child(curatorID)
                pathCurator
                    .addListenerForSingleValueEvent(AppValueEventListener { curator ->
                        numGroup = curator.getUserModel().Group
                    })
            })

        val code = enterCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                createToast("welcome")
                val person = User()
                person.id = AUTH.currentUser?.uid.toString()
                person.Phone = mLogin
                person.Group = numGroup
                updPhones(person)
                replaceFragment(MainListFragment())
            } else {
                MESS_ACTIVITY.createToast(it.exception.toString())
            }
        }
    }
}