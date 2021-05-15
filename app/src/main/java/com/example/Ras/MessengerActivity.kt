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
import com.example.Ras.Utils.*
import com.example.Ras.databinding.ActivityMessengerBinding
import com.example.Ras.objects.AppDrawer
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessengerActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mBinding: ActivityMessengerBinding

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
                Log.d("MyLog", "onStartMessA: start")
                Log.d("MyLog", "onStartMessA: ${USER.id}")
            }
            initFields()
            initFunc()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("MyLog", "onStartMessA: stop")
    }

    private fun initFields() {
        toolbar = toolbarMessenger as Toolbar
        mAppDrawer = AppDrawer(this, toolbar)
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(toolbar)
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
                val onlyPhone = selectedStrings[k].removeRange(13, selectedStrings[k].length)
                REF_DATABASE.child(NODE_PHONES).child(onlyPhone).setValue(UID)
                    .addOnSuccessListener {
                        createToast(getString(R.string.addedData))
                    }.addOnFailureListener {
                        Log.d("MyLog", "phonePick: $k bad")
                    }
            }
        }

        builder.show()
    }
}