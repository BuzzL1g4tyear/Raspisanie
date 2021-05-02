package com.example.Ras.Utils

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.Ras.R
import com.example.Ras.models.User

var actId: Int = 0

fun Fragment.createToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.data_container, fragment)
                .commit()
    } else {
        supportFragmentManager.beginTransaction()
                .replace(R.id.data_container, fragment)
                .commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.data_container,
                    fragment
            )?.commit()
}