package com.example.Ras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.AuthFragment
import com.example.Ras.Utils.AUTH_ACTIVITY
import com.example.Ras.databinding.ActivityAuthorizationBinding
import com.example.Ras.objects.AppDrawer
import kotlinx.android.synthetic.main.activity_authorization.*

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAuthorizationBinding
    private lateinit var mToolbarAuth: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        AUTH_ACTIVITY = this
        mToolbarAuth = toolbarAuth as Toolbar
        setSupportActionBar(mToolbarAuth)
        title = getString(R.string.auth_title)
        supportFragmentManager.beginTransaction()
            .add(R.id.data_container, AuthFragment())
            .commit()
        initFields()
    }

    private fun initFields() {
        mAppDrawer = AppDrawer(this, mToolbarAuth)
        mAppDrawer.create()
    }
}