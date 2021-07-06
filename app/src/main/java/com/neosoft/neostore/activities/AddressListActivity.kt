package com.neosoft.neostore.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.AddressAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.database.AddressDatabase
import com.neosoft.neostore.database.AddressEntity
import com.neosoft.neostore.fragments.MyOrdersFragment
import com.neosoft.neostore.models.PlaceOrderModel
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class AddressListActivity : AppCompatActivity(), View.OnClickListener, AddressAdapter.OnItemClick {
    //initialize variable
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var name: String
    private lateinit var address: String
    var add: String = ""
    private lateinit var token: String
    lateinit var myOrdersFragment: MyOrdersFragment
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.address_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()
        address = intent.getStringExtra("address").toString()
        name = sharedPreferences.getString("username", null).toString()
        setContentView(R.layout.activity_address_list)
        btn_place_order.setOnClickListener(this)
        //database creation
        val database = Room.databaseBuilder(
            this, AddressDatabase::class.java, "addressee's").allowMainThreadQueries().build()
        database.addressDao().insertAddress(AddressEntity(name = name, address = address))
        val allAddress = database.addressDao().getAddress()
        recycler_address_list.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = AddressAdapter(this@AddressListActivity, allAddress, this@AddressListActivity)
        }
    }
    override fun onClick(myAddress: String) {
        add = myAddress
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_address_menu, menu)
        return true
    }
        //option menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_add_address -> {
                val i = Intent(this, AddAddressActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_place_order ->
                //place order api
                placeOrder()
        }
    }
    private fun placeOrder() {
        retrofit.placeOrder(token, add).enqueue(object : Callback<PlaceOrderModel> {
            override fun onResponse(call: Call<PlaceOrderModel>, response: Response<PlaceOrderModel>)
            {
                try {
                    when {
                        response.code() == Constants.SUCESS_CODE -> {
                            toast(response.body()?.user_msg.toString())
                        }
                        response.code() == Constants.NOT_FOUND -> {
                            toast(response.body()?.user_msg.toString())
                        }
                        else -> {
                            toast(response.body()?.user_msg.toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<PlaceOrderModel>, t: Throwable) {
                toast(getString(R.string.no_connection))
            }
        })
    }

}
