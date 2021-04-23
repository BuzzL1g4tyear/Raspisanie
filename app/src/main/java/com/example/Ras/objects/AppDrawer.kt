package com.example.Ras.objects

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.Ras.AuthorizationActivity
import com.example.Ras.MainActivity
import com.example.Ras.MessengerActivity
import com.example.Ras.R
import com.example.Ras.UI.missingUI.MissingActivity
import com.example.Ras.Utils.AUTH
import com.example.Ras.Utils.replaceActivity
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class AppDrawer(val activity: AppCompatActivity, val toolbar: Toolbar) {

    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawer: Drawer
    private lateinit var mDrawerLayout: DrawerLayout

    fun create() {
        showHeader()
        showDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            when (activity) {
                is MessengerActivity -> {
                    activity.supportFragmentManager.popBackStack()
                }
                is MissingActivity -> {
                    activity.supportFragmentManager.popBackStack()
                }
                is AuthorizationActivity -> {
                    activity.supportFragmentManager.popBackStack()
                }
            }
        }
    }

    fun enableDrawer() {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            when (activity) {
                is MessengerActivity -> {
                    mDrawer.openDrawer()
                }
                is MissingActivity -> {
                    mDrawer.openDrawer()
                }
                is AuthorizationActivity -> {
                    mDrawer.openDrawer()
                }
            }
        }
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
                        PrimaryDrawerItem().withName(R.string.missing).withIcon(R.drawable.ic_report_24).withIdentifier(3),
                        SectionDrawerItem().withName(R.string.additionally),
//                        SecondaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings_24).withIdentifier(4),
                        SecondaryDrawerItem().withName(R.string.exit).withIcon(R.drawable.ic_logout_24).withIdentifier(5)
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, i: Int, drawerItem: IDrawerItem<*>): Boolean {
                        when (i) {
                            1 -> {
                                val intent1 = Intent(activity, MainActivity::class.java)
                                intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                activity.startActivity(intent1)
                            }
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
                            5 -> {
                                AUTH.signOut()
                                activity.replaceActivity(MainActivity())
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