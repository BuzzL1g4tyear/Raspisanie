package com.example.Ras.UI.missingUI

import androidx.fragment.app.Fragment
import com.example.Ras.R
import com.example.Ras.Utils.*
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User

class AdminMissingFragment : Fragment(R.layout.fragment_admin_missing) {
    //todo path
    private val mRefMiss = REF_DATABASE.child(NODE_MISSING).child("08:06:2021")
    private var mList = listOf<MissingPers>()

    override fun onStart() {
        super.onStart()
        initUser {
            mRefMiss.addListenerForSingleValueEvent(AppValueEventListener { data ->
                mList = data.children.map {
                    it.getValue(MissingPers::class.java) ?: MISSING
                }
            })
        }
    }
}