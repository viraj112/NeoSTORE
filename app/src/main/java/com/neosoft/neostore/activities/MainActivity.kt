package com.neosoft.neostore.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.neosoft.neostore.R
import com.neosoft.neostore.fragments.*
import com.neosoft.neostore.utilities.SessionManagement
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.navigation_header.view.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //declare variables
    private lateinit var session: SessionManagement
    private lateinit var myCartFragment: MyCartFragment
    private lateinit var tablesFragment: TablesFragment
    private lateinit var sofasFragment: SofasFragment
    private lateinit var chairsFragment: ChairsFragment
    lateinit var cupboardsFragment: CupboardsFragment
    private lateinit var myAccountFragment: MyAccountFragment
    lateinit var storeLocatorFragment: StoreLocatorFragment
    private lateinit var myOrdersFragment: MyOrdersFragment
    private lateinit var preferences: SharedPreferences
    private lateinit var count: TextView
    private lateinit var homeFragment: HomeFragment

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //session for login
        session = SessionManagement(this)
        session.checkLogin()

        //initialization of variables
        initialization()
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, homeFragment)
                commit()
            }
        }
    }

    //for variables initialization
    private fun initialization() {

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val email = preferences.getString("email", "")
        val username = preferences.getString("username", "")
        val image = preferences.getString("pic", "")
        //set values for header view
        val headerView: View = navigation_view.getHeaderView(0)
        headerView.txt_navigation_email.text = email
        headerView.txt_navigation_username.text = username
        Glide.with(this).load(image).into(headerView.circleImageView)

        //for toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.app_name)

        val drawerToggle: ActionBarDrawerToggle = object :
            ActionBarDrawerToggle(this, drawer_layout, toolbar, (R.string.open), (R.string.close)) {
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        drawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)

        navigation_view.setNavigationItemSelectedListener(this)
        count = MenuItemCompat.getActionView(navigation_view.menu.findItem(R.id.menu_my_cart)) as TextView

        initDrawer()
    }

    private fun initDrawer() {
        count.gravity = Gravity.CENTER
        count.fitsSystemWindows = true
        count.setTextColor(resources.getColor(R.color.white))
        count.text = "1"
    }

    //for  search menu item
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }
    //navigation menu click
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_my_cart ->
                //my cart fragment
                myCartsFragment()

            R.id.menu_table ->
                //tables fragment
                getTableFragment()

            R.id.menu_sofas ->

                //sofas fragment
                getSofasFragment()

            R.id.menu_chairs ->
                //chairs fragment
                getChairsFragment()

            R.id.menu_cupboards ->
                //cupboards fragment
                getCupboardsFragment()

            R.id.menu_my_account ->
                //my account fragment
                getMyAccountFragment()

            R.id.menu_store_locator ->
                //store locator fragment
                getStoreLocatorFragment()

            R.id.menu_my_orders ->
                //my orders fragment
                getMyOrderFragment()

            R.id.menu_logout ->
                //for logout user
                session.logoutUser()

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun getMyOrderFragment() {
        myOrdersFragment = MyOrdersFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myOrdersFragment)
            addToBackStack("backFrag")
            commit()
        }

    }

    private fun getStoreLocatorFragment() {
        toolbar.title = getString(R.string.store_locator)
        storeLocatorFragment = StoreLocatorFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, storeLocatorFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    private fun getMyAccountFragment() {
        toolbar.title = getString(R.string.my_account)
        myAccountFragment = MyAccountFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myAccountFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    private fun myCartsFragment() {
        toolbar.title = getString(R.string.my_cart)
        myCartFragment = MyCartFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myCartFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    private fun getTableFragment() {
        toolbar.title = getString(R.string.tables)
        tablesFragment = TablesFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, tablesFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    private fun getSofasFragment() {
        toolbar.title = getString(R.string.sofas)
        sofasFragment = SofasFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, sofasFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    private fun getChairsFragment() {
        toolbar.title = getString(R.string.chairs)
        chairsFragment = ChairsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, chairsFragment)
            addToBackStack("fragBack")
            commit()
        }
    }

    private fun getCupboardsFragment() {
        toolbar.title = getString(R.string.cupboards)
        chairsFragment = ChairsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, chairsFragment)
            addToBackStack("fragBack")
            commit()
        }
    }
}