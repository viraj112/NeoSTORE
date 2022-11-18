package com.neosoft.neostore.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
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
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MyOrdersFragment : Fragment() {
    var listData: ArrayList<OrderModel> = ArrayList()
    var displayList:ArrayList<OrderModel> = ArrayList()
    lateinit var adapter: MyordersAdapter
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var loadingDialog: LoadingDialog
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)
        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        setHasOptionsMenu(true)
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
                            if (response.body()?.data == null) {
                                activity?.toast(getString(R.string.empty_orders))
                            }else
                            {
                                val data: ArrayList<OrderModel> = response.body()?.data!!
                                listData = data
                                displayList.addAll(listData)
                                setRecycler()
                            }
                        }, Constants.DELAY_TIME.toLong())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<OrderListModel>, t: Throwable) {
                loadingDialog.isDismiss()
                activity?.toast(getString(R.string.no_connection))
            }
        })
    }
    //set recycler list
    private fun setRecycler() {
        adapter = MyordersAdapter(requireContext(), displayList)
        recycler_view_orders?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        if (menuItem !=null)
        {
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = getString(R.string.search_order_id)
            searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()){
                    displayList.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    listData.forEach {
                        if (it.id.toString().contains(search)) {
                            displayList.add(it)
                        }
                    }
                    recycler_view_orders.adapter!!.notifyDataSetChanged()
                }else
                {
                    displayList.clear()
                    displayList.addAll(listData)
                    recycler_view_orders.adapter!!.notifyDataSetChanged()
                }
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = getString(R.string.my_orders)
    }
}