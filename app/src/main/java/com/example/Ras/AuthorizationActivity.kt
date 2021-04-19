package com.example.Ras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.AuthFragment
import com.example.Ras.databinding.ActivityAuthorizationBinding
import kotlinx.android.synthetic.main.activity_authorization.*

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAuthorizationBinding
    private lateinit var mToolbarAuth: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        mToolbarAuth = toolbarAuth as Toolbar
        setSupportActionBar(mToolbarAuth)
        title = getString(R.string.auth_title)
        supportFragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.authData_container, AuthFragment())
                .commit()
    }
}