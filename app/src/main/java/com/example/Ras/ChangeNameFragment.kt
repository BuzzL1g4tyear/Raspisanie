package com.example.Ras

import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {
    private lateinit var mName: String
    private lateinit var mSurname: String
    override fun onStart() {
        super.onStart()
        (activity as MessengerActivity).mAppDrawer.disableDrawer()
        change_name_btn.setOnClickListener(View.OnClickListener {
            mName=change_name.text.toString()
            mSurname = change_surname.text.toString()
        })
    }

    override fun onStop() {
        super.onStop()
        (activity as MessengerActivity).mAppDrawer.enableDrawer()
    }
}