package com.example.Ras.messUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.Ras.databinding.FragmentChatBinding

open class BaseFragment(var layout: Int) : Fragment() {

    private lateinit var mRootView : View

    private lateinit var mBinding: FragmentChatBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        mRootView = inflater.inflate(layout,container,false)
        return mRootView
    }

    override fun onResume() {
        super.onResume()
    }
}