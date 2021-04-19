package com.example.Ras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.ChatFragment
import com.example.Ras.Utils.AUTH
import com.example.Ras.Utils.replaceActivity
import com.example.Ras.Utils.replaceFragment
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.objects.AppDrawer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messenger.*

class MessengerActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var mAppDrawer: AppDrawer
    private lateinit var mBinding: ActivityMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
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
            replaceFragment(ChatFragment())
        } else {
            replaceActivity(AuthorizationActivity())
        }
    }
}