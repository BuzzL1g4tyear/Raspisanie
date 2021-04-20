package com.example.Ras.Utils

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Ras.R
import com.example.Ras.UI.messUI.AuthFragment

var actId: Int = 0
    get() {
        return field
    }

fun Fragment.createToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().addToBackStack(null)
            .add(R.id.data_container, fragment)
            .commit()
}