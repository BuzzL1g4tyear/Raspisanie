package com.example.Ras.Utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONT = Manifest.permission.READ_CONTACTS
const val REQ_CODE = 200

fun checkPermission( permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            MESS_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(MESS_ACTIVITY, arrayOf(permission), REQ_CODE)
        false
    } else true
}