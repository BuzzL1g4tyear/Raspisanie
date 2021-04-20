package com.example.Ras.UI.messUI

import com.example.Ras.R
import com.example.Ras.objects.AppDrawer

class ChatFragment : BaseFragment(R.layout.fragment_chat) {



    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.messenger)

    }
}