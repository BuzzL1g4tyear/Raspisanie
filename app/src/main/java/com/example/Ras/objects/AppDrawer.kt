package com.example.Ras.objects

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.MissingActivity
import com.example.Ras.R
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class AppDrawer (val activity: AppCompatActivity,val toolbar: Toolbar) {

    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawer: Drawer

    fun create() {
        showHeader()
        showDrawer()
    }

    private fun showDrawer() {
        mDrawer = DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.timetable).withIcon(R.drawable.ic_schedule_24).withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.messenger).withIcon(R.drawable.ic_message_24).withIdentifier(2),
                        PrimaryDrawerItem().withName(R.string.missing).withIcon(R.drawable.ic_report_24).withIdentifier(3)
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, i: Int, drawerItem: IDrawerItem<*>): Boolean {
                        when (i) {
                            2 -> {
                                val intent2 = Intent(activity, MessengerActivity::class.java)
                                intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                activity.startActivity(intent2)
                            }
                            3 -> {
                                val intent3 = Intent(activity.applicationContext, MissingActivity::class.java)
                                intent3.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                activity.startActivity(intent3)
                            }
                        }
                        return false
                    }
                })
                .build()
    }

    private fun showHeader() {
        mHeader = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        ProfileDrawerItem().withName("Valentin")
                                .withEmail("123")
                                .withIcon(R.drawable.ic_school_24)
                )
                .build()
    }


}