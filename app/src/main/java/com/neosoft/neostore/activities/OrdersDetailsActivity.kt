package com.neosoft.neostore.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
class OrdersDetailsActivity : AppCompatActivity() {
    //initialize variables
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var adapter: OrderDetailsAdapter
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences
    var listData: List<OrderDetails> = ArrayList()
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
                        val list: List<OrderDetails> = items?.order_details!!
                        listData = list
                        adapter = OrderDetailsAdapter(this@OrdersDetailsActivity, list)
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
}