package com.example.Ras.UI.messUI

import androidx.fragment.app.Fragment
import com.example.Ras.R

class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.messenger)
    }
}