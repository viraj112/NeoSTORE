package com.neosoft.neostore.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.OrderDetailsAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.OrderDetails
import com.neosoft.neostore.models.OrderDetailsModel
import kotlinx.android.synthetic.main.activity_orders_details.*
import kotlinx.android.synthetic.main.fragment_my_cart.*
import kotlinx.android.synthetic.main.fragment_my_orders.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class OrdersDetailsActivity : AppCompatActivity() {
    //initialize variables
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var adapter: OrderDetailsAdapter
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences
    var listData: ArrayList<OrderDetails> = ArrayList()
    var displayList:ArrayList<OrderDetails> = ArrayList()
    private var orderId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        orderId = intent.getIntExtra("id", 1)
        sharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()
        supportActionBar?.title = "ORDER ID :  $orderId"
        recycler_orders_details.layoutManager = LinearLayoutManager(this)
        recycler_orders_details.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        getOrderDetails()
    }
    private fun getOrderDetails() {
        retrofit.getOrderDetails(token, orderId).enqueue(object : Callback<OrderDetailsModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<OrderDetailsModel>, response: Response<OrderDetailsModel>) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        val items = response.body()?.data
                        txt_cost_orders_details.text = "₹" + items?.cost.toString() + ".00"
                        val list: ArrayList<OrderDetails> = items?.order_details!!
                        listData = list
                        displayList.addAll(listData)
                        adapter = OrderDetailsAdapter(this@OrdersDetailsActivity,displayList)
                        recycler_orders_details.adapter = adapter
                    } else if (response.code() == Constants.NOT_FOUND) {
                        toast(response.body()?.message.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<OrderDetailsModel>, t: Throwable) {
                toast(t.message.toString())
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_here)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
             if(newText !=null)
             {
                 displayList.clear()
                 val search = newText.toLowerCase(Locale.getDefault())
                 listData.forEach {
                     if (it.prod_name.toLowerCase(Locale.getDefault()).contains(search)){
                         displayList.add(it)
                     }
                 }
                 recycler_orders_details.adapter!!.notifyDataSetChanged()
             }else{
                 displayList.clear()
                 displayList.addAll(listData)
                 recycler_orders_details.adapter!!.notifyDataSetChanged()
             }
                return true
               }

        })
        return true
    }

}