package com.neosoft.neostore.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.MyCartAdapter
import com.neosoft.neostore.adapters.OrderDetailsAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.Data
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
    val retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var adapter: OrderDetailsAdapter
    var listdata: List<OrderDetails> = ArrayList<OrderDetails>()
    var order_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        order_id = intent.getIntExtra("id",1)
        supportActionBar?.title="ORDER ID"+" :  "+order_id.toString()
        recycler_orders_details.layoutManager= LinearLayoutManager(this)
        recycler_orders_details.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))



        getOrderDetails()
    }

    private fun getOrderDetails() {

        retrofit.getOrderDetails(Constants.TOKEN,order_id).enqueue(object :Callback<OrderDetailsModel>{
            override fun onResponse(
                call: Call<OrderDetailsModel>,
                response: Response<OrderDetailsModel>
            ) {

                try {
                    if (response.code() == Constants.SUCESS_CODE)
                    {
                        val items =response.body()?.data
                        txt_cost_orders_details.setText("₹"+items?.cost.toString()+".00")
                        val list:List<OrderDetails> = items?.order_details!!
                        listdata=list
                        adapter = OrderDetailsAdapter(this@OrdersDetailsActivity,list)
                        recycler_orders_details.adapter = adapter

                    }else if (response.code() == Constants.NOT_FOUND)
                    {
                        toast(response.body()?.message.toString())
                    }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<OrderDetailsModel>, t: Throwable) {
                toast(t.message.toString())
            }
        })
    }
}