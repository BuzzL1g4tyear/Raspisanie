package com.example.Ras.Utils

import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Ras.models.PhoneUser
import com.example.Ras.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE: DatabaseReference
lateinit var USER: User
lateinit var arrayCont: ArrayList<PhoneUser>

const val NODE_USERS = "USERS"
const val NODE_LESSONS = "LESSONS"
const val NODE_MISSING = "MISSING_PERSONS"
const val NODE_PHONES = "PHONES"

const val CHILD_ID = "id"
const val CHILD_FULLNAME = "FullName"
const val CHILD_EMAIL = "Email"
const val CHILD_GROUP = "Group"
const val CHILD_PHONE = "Phone"
const val CHILD_TEXT = "Text"
const val CHILD_TYPE = "Type"
const val CHILD_FROM = "From"
const val CHILD_TIME = "TimeStamp"
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
        })
}

fun initContacts() {
    if (checkPermission(READ_CONT)) {
        arrayCont = arrayListOf()
        val cursor = MESS_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newPhoneUser = PhoneUser()
                newPhoneUser.name = fullName
                newPhoneUser.phone = phone.replace(Regex("[\\s,-]"), "")
                arrayCont.add(newPhoneUser)

            }
        }
        cursor?.close()
        getPickedNumbers(arrayCont)
    }
}

fun getPickedNumbers(arrayCont: ArrayList<PhoneUser>): Array<String> {
    val array: Array<String> = Array((arrayCont.size)) { arrayCont[1].toString() }
    var i = 0
    arrayCont.forEach { contact ->
        array[i] = "${contact.phone} (${contact.name})"
        i += 1
    }
    return array
}

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()