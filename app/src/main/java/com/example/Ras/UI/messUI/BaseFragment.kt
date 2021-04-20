package com.example.Ras.UI.messUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MainActivity
import com.example.Ras.databinding.FragmentChatBinding

open class BaseFragment(var layout: Int) : Fragment() {
    private lateinit var mRootView : View

    private lateinit var mBinding: FragmentChatBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        mRootView = inflater.inflate(layout,container,false)
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        (activity as AuthorizationActivity).mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as AuthorizationActivity).mAppDrawer.enableDrawer()
    }
}