package com.neosoft.neostore.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.MyordersAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.OrderListModel
import com.neosoft.neostore.models.OrderModel
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.fragment_my_orders.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


@Suppress("DEPRECATION")
class MyOrdersFragment : Fragment() {

    var listData: List<OrderModel> = ArrayList()
    lateinit var adapter: MyordersAdapter
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var loadingDialog: LoadingDialog
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)
        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()

        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view_orders.layoutManager = LinearLayoutManager(activity)
        recycler_view_orders.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        //get orders list
        getOrderList()
    }

    private fun getOrderList() {
        //api call for orders
        retrofit.getOrderList(token).enqueue(object : Callback<OrderListModel> {
            override fun onResponse(call: Call<OrderListModel>, response: Response<OrderListModel>) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        val handler = Handler()
                        handler.postDelayed({
                            loadingDialog.isDismiss()
                            val data: List<OrderModel> = response.body()?.data!!
                            listData = data

                            setRecycler()
                        }, Constants.DELAY_TIME.toLong())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<OrderListModel>, t: Throwable) {
                loadingDialog.isDismiss()
                activity?.toast(t.message.toString())
            }
        })
    }

    //set recycler list
    private fun setRecycler() {
        adapter = MyordersAdapter(requireContext(), listData)
        recycler_view_orders?.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}