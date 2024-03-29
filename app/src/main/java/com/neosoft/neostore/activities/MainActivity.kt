package com.neosoft.neostore.activities

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
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
    private lateinit var homeFragment: HomeFragment
    private var counterView: TextView? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //session for login
        session = SessionManagement(this)
        session.checkLogin()
        //initialization of variables
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initialization()
        }
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            replaceFragment(homeFragment)
        }
    }

    //for variables initialization
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        val count = preferences.getString("count", "0")
        val menuItem = navigation_view.menu.findItem(R.id.menu_my_cart)
        val view = menuItem.setActionView(R.layout.cart_count)
        counterView = view.actionView?.findViewById(R.id.txt_cart_count)
        counterView?.text = count

    }

    //navigation menu click
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_my_cart ->
                //my cart fragment
                replaceFragment(myCartFragment)

            R.id.menu_table ->
                //tables fragment
                replaceFragment(tablesFragment)
            // getTableFragment()

            R.id.menu_sofas ->
                //sofas fragment
                replaceFragment(sofasFragment)
            // getSofasFragment()

            R.id.menu_chairs ->
                //chairs fragment
                replaceFragment(chairsFragment)
            // getChairsFragment()

            R.id.menu_cupboards ->
                //cupboards fragment
                replaceFragment(cupboardsFragment)
            // getCupboardsFragment()

            R.id.menu_my_account ->
                //my account fragment
                replaceFragment(myAccountFragment)
            //     getMyAccountFragment()

            R.id.menu_store_locator ->
                //store locator fragment
                //   getStoreLocatorFragment()
                replaceFragment(storeLocatorFragment)
            R.id.menu_my_orders ->
                //my orders fragment
                replaceFragment(myOrdersFragment)
               // getMyOrderFragment()

            R.id.menu_logout ->
                //for logout user
                session.logoutUser()

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getMyOrderFragment() {
        toolbar.title = getString(R.string.my_orders)
        myOrdersFragment = MyOrdersFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myOrdersFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getStoreLocatorFragment() {
        toolbar.title = getString(R.string.store_locator)
        storeLocatorFragment = StoreLocatorFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, storeLocatorFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getMyAccountFragment() {
        toolbar.title = getString(R.string.my_account)
        myAccountFragment = MyAccountFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myAccountFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun myCartsFragment() {
        toolbar.title = getString(R.string.my_cart)
        myCartFragment = MyCartFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, myCartFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getTableFragment() {
        toolbar.title = getString(R.string.tables)
        tablesFragment = TablesFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, tablesFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getSofasFragment() {
        toolbar.title = getString(R.string.sofas)
        sofasFragment = SofasFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, sofasFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getChairsFragment() {
        toolbar.title = getString(R.string.chairs)
        chairsFragment = ChairsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, chairsFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun getCupboardsFragment() {
        toolbar.title = getString(R.string.cupboards)
//
        replaceFragment(cupboardsFragment)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}