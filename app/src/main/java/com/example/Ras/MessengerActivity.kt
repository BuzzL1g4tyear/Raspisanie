package com.example.Ras

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.Ras.UI.messUI.ChatFragment
import com.example.Ras.UI.messUI.SingleChatFragment
import com.example.Ras.Utils.*
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.models.PhoneUser
import com.example.Ras.models.User
import com.example.Ras.objects.AppDrawer
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessengerActivity : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mBinding: ActivityMessengerBinding
    private val mListPhones= arrayListOf<PhoneUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        actId = 1
    }

    override fun onStart() {
        super.onStart()
        MESS_ACTIVITY = this
        initDatabase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }
    }

    private fun initFields() {
        mToolbar = toolbarMessenger as Toolbar
        mAppDrawer = AppDrawer(this, mToolbar)

    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatFragment(), false)
        } else {
            replaceActivity(AuthorizationActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }

    fun phonePick() {
        val items = getPickedNumbers(arrayCont)
        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.pickTitle)
        builder.setMultiChoiceItems(
            items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val selectedStrings = ArrayList<String>()

            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])

            }
            for (k in selectedList.indices) {
                val number = PhoneUser()//nbm
                val onlyPhone = selectedStrings[k].removeRange(13, selectedStrings[k].length)
                number.Phone = onlyPhone.trim()
                mListPhones.add(number)
            }
            mListPhones.forEach {
                REF_DATABASE.child(NODE_PHONES)
                    .child(it.Phone)
                    .setValue(UID)
                    .addOnSuccessListener {
                        createToast(getString(R.string.addedData))
                    }.addOnFailureListener { err ->
                        Log.d("MyLog", err.message.toString())
                    }
            }
        }

        builder.show()
    }
}