package com.neosoft.neostore.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import com.neosoft.neostore.R
import com.neosoft.neostore.fragments.*
import com.neosoft.neostore.utilities.SessionManagement
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var session :SessionManagement
    lateinit var myCartFragment: MyCartFragment
    lateinit var tablesFragment: TablesFragment
    lateinit var sofasFragment: SofasFragment
    lateinit var chairsFragment: ChairsFragment
    lateinit var cupboardsFragment: CupboardsFragment
    lateinit var myAccountFragment: MyAccountFragment
    lateinit var storeLocatorFragment: StoreLocatorFragment
    lateinit var myOrdersFragment: MyOrdersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManagement(this)

        session.checkLogin()
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.ic_slider_image_one,""))
        imageList.add(SlideModel(R.drawable.ic_slider_image_two,""))
        imageList.add(SlideModel(R.drawable.ic_image_slider_three,""))
        imageList.add(SlideModel(R.drawable.ic_image_slider_four,""))

        image_slider.setImageList(imageList)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title =getString(R.string.app_name)

        val drawerToggle :ActionBarDrawerToggle = object :ActionBarDrawerToggle (
            this,
            drawer_layout,
            toolbar,
            (R.string.open),
            (R.string.close)
        ){

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        drawerToggle?.drawerArrowDrawable?.color = ContextCompat.getColor(this, R.color.white)
        navigation_view.setNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when(menuItem.itemId)
        {
        R.id.menu_my_cart ->
        {
            myCartFragment = MyCartFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout,myCartFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
            R.id.menu_table ->
            {
                tablesFragment = TablesFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout,tablesFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.menu_logout ->
            {
                session.logoutUser()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else
        {
            super.onBackPressed()
        }

    }
}