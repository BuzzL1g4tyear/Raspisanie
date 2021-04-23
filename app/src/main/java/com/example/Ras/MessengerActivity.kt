package com.example.Ras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.ChatFragment
import com.example.Ras.Utils.*
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.models.User
import com.example.Ras.objects.AppDrawer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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
        initFields()
        initFunc()
    }

    private fun initFields() {
        toolbar = toolbarMessenger as Toolbar
        mAppDrawer = AppDrawer(this, toolbar)
        AUTH = FirebaseAuth.getInstance()
        initUser()
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

    private fun initUser() {
        REF_DATABASE.child(NODE_USERS).child(UID)
                .addListenerForSingleValueEvent(AppValueEventListener {
                     USER = it.getValue(User::class.java) ?: User()
                })
    }
}