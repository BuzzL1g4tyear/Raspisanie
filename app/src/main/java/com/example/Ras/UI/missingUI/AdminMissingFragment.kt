package com.example.Ras.UI.missingUI

import android.app.DatePickerDialog
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.AddPhoneAdapter
import com.example.Ras.AdminMissingAdapter
import com.example.Ras.R
import com.example.Ras.Utils.*
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_add_phone.*
import kotlinx.android.synthetic.main.fragment_admin_missing.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class AdminMissingFragment : Fragment(R.layout.fragment_admin_missing) {

    private val mRefMiss = REF_DATABASE.child(NODE_MISSING)
    private lateinit var mRecyclerView: RecyclerView
    private var mList = listOf<MissingPers>()
    private lateinit var mAdapter: AdminMissingAdapter
    private var date = ""

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    private fun initMissingPers(date: String) {
        mRecyclerView = admin_missing_rv
        mAdapter = AdminMissingAdapter()
        mRefMiss.child(date).addListenerForSingleValueEvent(AppValueEventListener { data ->
            mList = data.children.map {
                it.getValue(MissingPers::class.java) ?: MISSING
            }
            mList.forEach {
                mAdapter.updateListItems(it)
            }
        })
        mRecyclerView.adapter = mAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MissingActivity).menuInflater.inflate(R.menu.items_admin_miss, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.calendar_picker_admin -> {
                datePick()
            }
        }
        return true
    }

    private fun datePick() {
        val c = Calendar.getInstance()
        val yearR = c.get(Calendar.YEAR)
        val monthR = c.get(Calendar.MONTH)
        val dayR = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->

            val dateFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
            c.set(year, monthOfYear, dayOfMonth)
            date = dateFormat.format(c.time)
            initMissingPers(date)
            createToast(date)
        }, yearR, monthR, dayR)
        dpd.show()
    }

    companion object {
        val listMissItems = arrayListOf<MissingPers>()
    }
}