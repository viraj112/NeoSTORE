package com.neosoft.neostore.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.AddressAdapter
import com.neosoft.neostore.models.AddressModel
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : AppCompatActivity() {
    val list =ArrayList<AddressModel>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var addressAdapter: AddressAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.address_list)

        sharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        val name = sharedPreferences.getString("username",null)
        setContentView(R.layout.activity_address_list)

        recycler_address_list.layoutManager = LinearLayoutManager(this)
        addressAdapter = AddressAdapter(this,list)
        recycler_address_list.adapter = addressAdapter
        val address = intent.getStringExtra("address")
            list.add(AddressModel(name.toString(),address.toString()))
            addressAdapter.notifyDataSetChanged()

    }
    //for  add address menu item
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_address_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)
        {
            R.id.menu_add_address ->
            {
                val i:Intent = Intent(this,AddAddressActivity::class.java)
                startActivity(i)
                return true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }
}