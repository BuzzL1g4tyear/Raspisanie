package com.example.Ras.UI.missingUI

import android.app.DatePickerDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.Ras.AdminMissingAdapter
import com.example.Ras.R
import com.example.Ras.Utils.*
import com.example.Ras.models.MissingPers
import com.example.Ras.models.User
import kotlinx.android.synthetic.main.fragment_admin_missing.*
import java.text.SimpleDateFormat
import java.util.*

class AdminMissingFragment : Fragment(R.layout.fragment_admin_missing) {

    private lateinit var shareActionProvider: androidx.core.view.ActionProvider
    private val mRefMiss = REF_DATABASE.child(NODE_MISSING)
    private lateinit var mRecyclerView: RecyclerView
    private var mList = listOf<MissingPers>()
    private var mListPer = mutableListOf<User>()
    private lateinit var mAdapter: AdminMissingAdapter
    private var date = ""
    private var group = ""
    private var surname = ""
    private var messageForSend = ""

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        b1.setOnClickListener {
            shareList(mListPer)
        }
    }

    private fun initMissingPers(date: String) {
        mRecyclerView = admin_missing_rv
        mAdapter = AdminMissingAdapter()
        mRefMiss.child(date).addListenerForSingleValueEvent(AppValueEventListener { data ->
            mList = data.children.map {
                it.getValue(MissingPers::class.java) ?: MISSING
            }
            mList.forEach {
                for (element in it.Disease) {
                    val missPers = User()
                    missPers.Group = it.Group
                    missPers.TimeStamp = it.TimeStamp
                    missPers.Surname = element
                    missPers.Cause = getString(R.string.disease)
                    mListPer.add(missPers)
                }
                for (element in it.Order) {
                    val missPers = User()
                    missPers.Group = it.Group
                    missPers.TimeStamp = it.TimeStamp
                    missPers.Surname = element
                    missPers.Cause = getString(R.string.order)
                    mListPer.add(missPers)
                }
                for (element in it.Statement) {
                    val missPers = User()
                    missPers.Group = it.Group
                    missPers.TimeStamp = it.TimeStamp
                    missPers.Surname = element
                    missPers.Cause = getString(R.string.petition)
                    mListPer.add(missPers)
                }
                for (element in it.Reason) {
                    val missPers = User()
                    missPers.Group = it.Group
                    missPers.TimeStamp = it.TimeStamp
                    missPers.Surname = element
                    missPers.Cause = getString(R.string.disrespectful)
                    mListPer.add(missPers)
                }
            }
            mListPer.forEach {
                mAdapter.updateListItems(it)
            }
        })
        mRecyclerView.adapter = mAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MissingActivity).menuInflater.inflate(R.menu.items_admin_miss, menu)
        val menuItem = menu.findItem(R.id.share_list_admin)
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider

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

    private fun shareList(list: MutableList<User>) {

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
        }
        messageForSend =
            "В колледже отсутствует ${mRecyclerView.layoutManager?.childCount}\n"

        list.forEach {
            group = it.Group
            surname = it.Surname
            messageForSend += group + " " + surname.trim() + "\n"
        }

        intent.putExtra(Intent.EXTRA_TEXT, messageForSend)
        (shareActionProvider as ShareActionProvider).setShareIntent(intent)
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
            b1.visibility = View.VISIBLE
            b1.isClickable = true
            b1.isFocusable = true
        }, yearR, monthR, dayR)
        dpd.show()
    }
}