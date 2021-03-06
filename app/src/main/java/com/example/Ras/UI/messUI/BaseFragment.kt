package com.example.Ras.UI.messUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.AUTH_ACTIVITY
import com.example.Ras.Utils.MESS_ACTIVITY

open class BaseFragment(var layout: Int) : Fragment() {
    private lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mRootView = inflater.inflate(layout, container, false)
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        when (activity) {
            is AuthorizationActivity -> {
                AUTH_ACTIVITY.mAppDrawer.disableDrawer()
            }
            is MissingActivity -> {
                (activity as MissingActivity).mAppDrawer.disableDrawer()
            }
            is MessengerActivity -> {
                MESS_ACTIVITY.mAppDrawer.disableDrawer()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        when (activity) {
            is AuthorizationActivity -> {
                (activity as AuthorizationActivity).mAppDrawer.enableDrawer()
            }
            is MissingActivity -> {
                (activity as MissingActivity).mAppDrawer.enableDrawer()
            }
            is MessengerActivity -> {
                (activity as MessengerActivity).mAppDrawer.enableDrawer()
            }
        }
    }
}