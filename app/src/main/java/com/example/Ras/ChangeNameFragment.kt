package com.example.Ras

import androidx.fragment.app.Fragment

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {
    override fun onStart() {
        super.onStart()
        (activity as MessengerActivity).mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MessengerActivity).mAppDrawer.enableDrawer()
    }
}