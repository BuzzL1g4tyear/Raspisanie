package com.example.Ras

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.Ras.UI.messUI.ChatFragment
import com.example.Ras.Utils.*
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.objects.AppDrawer
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
            initContacts()
            initFields()
            initFunc()
        }
    }

    private fun initContacts() {
        if (checkPermission(this,READ_CONT)) {
            Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFields() {
        toolbar = toolbarMessenger as Toolbar
        mAppDrawer = AppDrawer(this, toolbar)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }
}