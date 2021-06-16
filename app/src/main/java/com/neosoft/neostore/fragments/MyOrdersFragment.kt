package com.neosoft.neostore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.MyordersAdapter
import com.neosoft.neostore.adapters.TablesAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.OrderListModel
import com.neosoft.neostore.models.OrderModel
import com.neosoft.neostore.models.ProductModel
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.fragment_tables.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MyOrdersFragment : Fragment() {

    var listdata: List<OrderModel> = ArrayList()
    lateinit var adapter: MyordersAdapter
    val retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view_orders.layoutManager = LinearLayoutManager(activity)
        recycler_view_orders.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        getOrderList()
    }

    private fun getOrderList() {

        retrofit.getOrderList(Constants.TOKEN).enqueue(object :Callback<OrderListModel>{
            override fun onResponse(
                call: Call<OrderListModel>,
                response: Response<OrderListModel>
            ) {
                try {
                    if (response.code()==Constants.SUCESS_CODE)
                    {
                        val data:List<OrderModel> =response.body()?.data!!
                        listdata =data
                        adapter = MyordersAdapter(context!!,listdata)
                        recycler_view_orders?.adapter = adapter
                        adapter.notifyDataSetChanged()


                    }

                }catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<OrderListModel>, t: Throwable) {
                activity?.toast(t.message.toString())
            }
        })
    }


}