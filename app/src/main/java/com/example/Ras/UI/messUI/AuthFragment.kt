package com.example.Ras.UI.messUI

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.Ras.R
import com.example.Ras.Utils.AUTH
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private lateinit var mLogin: String
    private lateinit var mPass: String
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
            authUser()
        }
    }

    private fun authUser() {
        mLogin = auth_Login.text.toString()
        mPass = auth_Pass.text.toString()

        AUTH.signInWithEmailAndPassword(mLogin, mPass).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                fragmentManager?.beginTransaction()?.addToBackStack(null)
                        ?.add(R.id.data_container, ChatFragment())
                        ?.commit()
            }
        }
    }
}