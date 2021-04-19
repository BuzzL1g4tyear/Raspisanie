package com.example.Ras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.Ras.UI.messUI.ChatFragment
import kotlinx.android.synthetic.main.activity_messenger.*

class MessengerActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar()
        initFunc()
    }

    private fun initFunc() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.data_container,ChatFragment()).commit()
    }

    private fun setToolbar() {
        setContentView(R.layout.activity_messenger)
        toolbar = toolbarMessenger as Toolbar
        setSupportActionBar(toolbar)
    }
}