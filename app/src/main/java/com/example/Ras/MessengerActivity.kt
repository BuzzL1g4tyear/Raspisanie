package com.example.Ras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.ChatFragment
import com.example.Ras.Utils.*
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.objects.AppDrawer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messenger.*

class MessengerActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mBinding: ActivityMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        actId = 1
    }

    override fun onStart() {
        super.onStart()
        initDatabase()
        initUser {
            initFields()
            initFunc()
        }
    }

    private fun initFields() {
        toolbar = toolbarMessenger as Toolbar
        mAppDrawer = AppDrawer(this, toolbar)
        AUTH = FirebaseAuth.getInstance()
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(toolbar)
            mAppDrawer.create()
            replaceFragment(ChatFragment(), false)

        } else {
            replaceActivity(AuthorizationActivity())
        }
    }

}