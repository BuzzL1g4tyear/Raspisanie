package com.example.Ras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Ras.databinding.ActivityMainBinding

class MessengerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        val mainAct = MainActivity()
        mainAct.showHeader()
        mainAct.showDrawer()
    }
}