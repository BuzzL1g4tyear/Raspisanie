package com.example.Ras

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.example.Ras.UI.messUI.BaseFragment
import com.example.Ras.Utils.*
import com.example.Ras.databinding.FragmentEditUserBinding
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment(val user: User) : BaseFragment(R.layout.fragment_edit_user) {

    private lateinit var mBinding: FragmentEditUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditUserBinding.inflate(layoutInflater)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        setFields()
        MESS_ACTIVITY.title = user.FullName
        setHasOptionsMenu(true)

    }

    private fun setFields() {
        mBinding.editUserGroup.setText(user.Group)
        mBinding.editUserEmail.setText(user.Email)
        var status = ""
        when (user.Status) {
            "2" -> {
                status = "Староста"
            }
            "3" -> {
                status = "Куратор"
            }
            "4" -> {
                status = "Администратор"
            }
        }
        status_spinner.setText(status)
        setSpinner()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MESS_ACTIVITY.menuInflater.inflate(R.menu.item_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.confirm_change_name -> {
                changeData()
            }
        }
        return true
    }

    private fun changeData() {
        val group = mBinding.editUserGroup.text.toString()
        val status = mBinding.statusSpinner.text.toString()

        var statusID = ""
        when (status) {
            "Староста" -> {
                statusID = "2"
            }
            "Куратор" -> {
                statusID = "3"
            }
            "Администратор" -> {
                statusID = "4"
            }
        }

        if (group.isNotEmpty() && status.isNotEmpty()) {
            val mapData = hashMapOf<String, Any>()
            val refUser = REF_DATABASE.child(NODE_USERS).child(user.id)

            mapData[CHILD_GROUP] = group
            mapData[CHILD_STATUS] = statusID

            refUser.updateChildren(mapData).addOnSuccessListener {
                createToast(getString(R.string.changedData))
            }
        } else {
            createToast(getString(R.string.auth_eEmpty_text))
        }
    }

    private fun setSpinner() {
        val statusArr = resources.getStringArray(R.array.status_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_status, statusArr)
        status_spinner.setAdapter(adapter)
    }
}