package com.example.Ras.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH : FirebaseAuth
lateinit var REF_DATABASE: DatabaseReference

const val NODE_USERS ="USERS"
const val NODE_LESSONS ="LESSONS"
const val MISSING_PERSONS = "MISSING_PERSONS"

const val CHILD_ID = "id"
const val CHILD_USERNAME = "UserName"
const val CHILD_EMAIL = "Email"

fun initDatabase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE = FirebaseDatabase.getInstance().reference
}