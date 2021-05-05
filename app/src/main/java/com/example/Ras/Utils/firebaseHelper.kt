package com.example.Ras.Utils

import android.util.Log
import com.example.Ras.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE: DatabaseReference
lateinit var USER: User

const val NODE_USERS = "USERS"
const val NODE_LESSONS = "LESSONS"
const val NODE_MISSING = "MISSING_PERSONS"

const val CHILD_ID = "id"
const val CHILD_FULLNAME = "FullName"
const val CHILD_EMAIL = "Email"
const val CHILD_STATUS = "Status"
const val CHILD_ORDER = "Order"
const val CHILD_DISEASE = "Disease"
const val CHILD_STATEMENT = "Statement"
const val CHILD_REASON = "Reason"

fun initDatabase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE = FirebaseDatabase.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(User::class.java) ?: User()
            function()
            Log.d("MyLog", "messAct: ${USER.id}")
        })
}