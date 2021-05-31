package com.example.Ras.Utils

import android.provider.ContactsContract
import android.util.Log
import com.example.Ras.MainActivity
import com.example.Ras.R
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.HashMap

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE: DatabaseReference
lateinit var USER: User
lateinit var MISSING: MissingPers
lateinit var arrayCont: ArrayList<User>

const val TYPE_TEXT = "Text"

const val TYPE_GROUP = "Group"

const val NODE_USERS = "USERS"
const val NODE_LESSONS = "LESSONS"
const val NODE_MISSING = "MISSING_PERSONS"
const val NODE_PHONES = "PHONES"
const val NODE_MESSAGES = "MESSAGES"
const val NODE_MEMBERS = "MEMBERS"
const val NODE_MAIN_LIST = "MAIN_LIST"
const val NODE_GROUP_CHAT = "GROUP_CHAT"

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
const val USER_MEMBER = "Member"
const val USER_CREATOR = "Creator"

fun initDatabase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE = FirebaseDatabase.getInstance().reference
    USER = User()
    MISSING = MissingPers()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(User::class.java) ?: User()
            function()
        })
}

inline fun initMissingPers(crossinline function: () -> Unit) {
    REF_DATABASE.child(NODE_MISSING).child(MainActivity.date)
        .child(USER.Group)
        .addListenerForSingleValueEvent(AppValueEventListener {
            MISSING = it.getValue(MissingPers::class.java) ?: MissingPers()
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
                val newPhoneUser = User()
                newPhoneUser.Name = fullName
                newPhoneUser.Phone = phone.replace(Regex("[\\s,-]"), "")
                arrayCont.add(newPhoneUser)

            }
        }
        cursor?.close()
        getPickedNumbers(arrayCont)
    }
}

fun getPickedNumbers(arrayCont: ArrayList<User>): Array<String> {
    val array: Array<String> = Array((arrayCont.size)) { arrayCont[1].toString() }
    var i = 0
    arrayCont.forEach { contact ->
        array[i] = "${contact.Phone} (${contact.Name})"
        i += 1
    }
    return array
}

//todo
fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$UID/n5yyZ34IPtSXtx62x4VhpXON2Q13"
    val refReceivingUser = "$NODE_MESSAGES/n5yyZ34IPtSXtx62x4VhpXON2Q13/$UID"

    val messageKey = REF_DATABASE.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIME] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { Log.d("MyLog", "sendMessage: ${it.message.toString()}") }
}

fun createGroup(
    numGroup: String,
    list: ArrayList<User>,
    function: () -> Unit
) {
    val keyGroup = REF_DATABASE.child(NODE_GROUP_CHAT).push().key.toString()
    val path = REF_DATABASE.child(NODE_GROUP_CHAT).child(keyGroup)

    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = keyGroup
    mapData[CHILD_FULLNAME] = numGroup

    val mapMembers = hashMapOf<String, Any>()
    list.forEach {
        mapMembers[it.Phone] = USER_MEMBER
    }
    mapMembers[UID] = USER_CREATOR
    mapData[NODE_MEMBERS] = mapMembers
    path.updateChildren(mapData)
        .addOnSuccessListener {
            MESS_ACTIVITY.createToast(MESS_ACTIVITY.getString(R.string.groupCreated))
            addGroupToMainList(mapData, list) {
                function()
            }
        }
}

fun addGroupToMainList(mapData: HashMap<String, Any>, list: List<User>, function: () -> Unit) {
    val path = REF_DATABASE.child(NODE_MAIN_LIST)

    val map = hashMapOf<String, Any>()

    map[CHILD_ID] = mapData[CHILD_ID].toString()
    map[CHILD_TYPE] = TYPE_GROUP

    list.forEach {
        path.child(it.id).child(map[CHILD_ID].toString())
            .updateChildren(map)
    }
    path.child(UID).child(map[CHILD_ID].toString())
        .updateChildren(map)
        .addOnSuccessListener { function() }
        .addOnFailureListener { MESS_ACTIVITY.createToast(it.message.toString()) }
}

fun saveToMainList(id: String, type: String) {
    val refUser = "$NODE_MAIN_LIST/$UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$UID"

    val mapUser = hashMapOf<String,Any>()
    val mapReceived = hashMapOf<String,Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = UID
    mapReceived[CHILD_TYPE] = type

    val mapCommon = hashMapOf<String,Any>()
    mapCommon[refUser] = mapUser
    mapReceived[refReceived] = mapReceived

    REF_DATABASE.updateChildren(mapCommon)
        .addOnFailureListener { MESS_ACTIVITY.createToast(it.message.toString()) }
}

fun sendGroupMessage(message: String, groupID: String, typeText: String, function: () -> Unit) {
    val refMessages = "$NODE_GROUP_CHAT/$groupID/$NODE_MESSAGES"

    val messageKey = REF_DATABASE.child(refMessages).push().key.toString()

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIME] = ServerValue.TIMESTAMP

    REF_DATABASE
        .child(refMessages)
        .child(messageKey)
        .updateChildren(mapMessage)
        .addOnSuccessListener { function() }
        .addOnFailureListener { Log.d("MyLog", "sendMessage: ${it.message.toString()}") }
}

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()