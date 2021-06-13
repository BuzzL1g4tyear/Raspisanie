package com.example.Ras.Utils

import android.provider.ContactsContract
import com.example.Ras.MainActivity
import com.example.Ras.R
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.*

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE: DatabaseReference
lateinit var USER: User
private var mCapitan = User()
lateinit var MISSING: MissingPers
lateinit var arrayCont: ArrayList<User>

const val TYPE_TEXT = "Text"

const val TYPE_GROUP = "Group"

const val NODE_USERS = "USERS"
const val NODE_LESSONS = "LESSONS"
const val NODE_MISSING = "MISSING_PERSONS"
const val NODE_PHONES = "PHONES"
const val NODE_AUTHPHONES = "AUTHPHONES"
const val NODE_MESSAGES = "MESSAGES"
const val NODE_MEMBERS = "MEMBERS"
const val NODE_MAIN_LIST = "MAIN_LIST"
const val NODE_GROUP_CHAT = "GROUP_CHAT"

const val CHILD_ID = "id"
const val CHILD_CREATOR_ID = "CreatorID"
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
const val CHILD_CAUSE = "Cause"
const val CHILD_DISEASE = "Disease"
const val CHILD_STATEMENT = "Statement"
const val CHILD_REASON = "Reason"
const val USER_MEMBER = "Member"
const val USER_CREATOR = "Creator"
const val USER_CAPITAN = "Capitan"

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

fun updPhones(
    person: User,
    curatorID: String
) {
    val pathAuthPhones = REF_DATABASE.child(NODE_AUTHPHONES)
    val pathPhones = REF_DATABASE.child(NODE_PHONES)
    val mapUser = hashMapOf<String, Any>()
    val mapPhone = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = person.id
    mapUser[CHILD_PHONE] = person.Phone

    mapPhone[CHILD_CREATOR_ID] = curatorID
    mapPhone[CHILD_ID] = person.id
    pathAuthPhones.child(person.Group).child(person.id).updateChildren(mapUser)
    pathPhones.child(person.Phone).updateChildren(mapPhone)
}

fun createGroup(
    numGroup: String,
    list: ArrayList<User>,
    function: () -> Unit
) {
    var mListPersons = listOf<User>()
    val mListPersonsID = arrayListOf<User>()

    val keyGroup = REF_DATABASE.child(NODE_GROUP_CHAT).push().key.toString()
    val pathGroup = REF_DATABASE.child(NODE_GROUP_CHAT).child(keyGroup)
    val pathUser = REF_DATABASE.child(NODE_USERS)
    val pathPhones = REF_DATABASE.child(NODE_PHONES)

    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = keyGroup
    mapData[CHILD_FULLNAME] = numGroup

    val mapMembers = hashMapOf<String, Any>()
    list.forEach {
        pathPhones.child(it.Phone).addListenerForSingleValueEvent(AppValueEventListener { person ->
            val id = person.getUserModel().id
            mListPersonsID.add(person.getUserModel())
            mapMembers[id] = USER_MEMBER
        })
    }

    pathUser.addListenerForSingleValueEvent(AppValueEventListener { Data ->
        mListPersons = Data.children.map { it.getUserModel() }

        mListPersons.forEach { person ->
            if (person.Group == numGroup && person.Status == "2") {
                mCapitan = person
                mapMembers[mCapitan.id] = USER_CAPITAN
                pathGroup.updateChildren(mapData)
            }
        }
    })

    mapMembers[UID] = USER_CREATOR
    mapData[NODE_MEMBERS] = mapMembers
    pathGroup.updateChildren(mapData)
        .addOnSuccessListener {
            MESS_ACTIVITY.createToast(MESS_ACTIVITY.getString(R.string.groupCreated))
            addGroupToMainList(mapData, mListPersonsID) {
                function()
            }
        }
}

fun addGroupToMainList(mapData: HashMap<String, Any>, list: List<User>, function: () -> Unit) {
    val pathMainList = REF_DATABASE.child(NODE_MAIN_LIST)

    val map = hashMapOf<String, Any>()

    map[CHILD_ID] = mapData[CHILD_ID].toString()
    map[CHILD_TYPE] = TYPE_GROUP

    list.forEach {
        pathMainList.child(it.id).child(map[CHILD_ID].toString())
            .updateChildren(map)
    }
    pathMainList.child(mCapitan.id).child(map[CHILD_ID].toString())
        .updateChildren(map)
    pathMainList.child(UID).child(map[CHILD_ID].toString())
        .updateChildren(map)
        .addOnSuccessListener { function() }
        .addOnFailureListener { MESS_ACTIVITY.createToast(it.message.toString()) }
}

//fun delete

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
}

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()