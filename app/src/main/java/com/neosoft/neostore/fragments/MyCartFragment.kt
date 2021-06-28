package com.neosoft.neostore.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.AddAddressActivity
import com.neosoft.neostore.adapters.MyCartAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.Data
import com.neosoft.neostore.models.MyCartListModel
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.fragment_my_cart.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
@Suppress("DEPRECATION")
class MyCartFragment : Fragment(), View.OnClickListener {
    //variable initialization
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var adapter: MyCartAdapter
    var listData: List<Data> = ArrayList()
    private lateinit var token: String
    lateinit var total: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var count: String
    lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()
        val view = inflater.inflate(R.layout.fragment_my_cart, container, false)
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        return view
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reecycler_my_cart.layoutManager = LinearLayoutManager(activity)
        reecycler_my_cart.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        //assign value to variable
        btn_my_cart_order_now.visibility = View.GONE
        txt_total_my_cart.visibility = View.GONE
        txt_sum_my_cart.visibility = View.GONE
        btn_my_cart_order_now.setOnClickListener(this)
        //getting cart items
        getCartList()
    }
    //api cll for cart list
    private fun getCartList() {
        retrofit.getCartList(token).enqueue(object : Callback<MyCartListModel> {
            override fun onResponse(call: Call<MyCartListModel>, response: Response<MyCartListModel>) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        val handler = Handler()
                        handler.postDelayed({
                            loadingDialog.isDismiss()
                            if (response.body()?.data == null) {
                                activity?.toast(getString(R.string.empty_cart))
                            } else {
                                listData = response.body()?.data!!
                                total = response.body()?.total.toString()
                                count = response.body()?.count.toString()
                                setRecycler()
                                visibility()
                            }
                        }, Constants.DELAY_TIME.toLong())
                    } else {
                        loadingDialog.isDismiss()
                        activity?.toast(response.message().toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<MyCartListModel>, t: Throwable) {
                loadingDialog.isDismiss()
                context?.toast(R.string.no_connection)
            }
        })
    }
    //set recycler view
    private fun setRecycler() {
        adapter = MyCartAdapter(requireActivity(), listData)
        reecycler_my_cart.adapter = adapter
        adapter.notifyDataSetChanged()
        txt_sum_my_cart.text = total
    }
    //for views visibility
    private fun visibility() {
        btn_my_cart_order_now.visibility = View.VISIBLE
        txt_total_my_cart.visibility = View.VISIBLE
        txt_sum_my_cart.visibility = View.VISIBLE
    }
    // button click
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_my_cart_order_now -> {
                val i = Intent(activity, AddAddressActivity::class.java)
                startActivity(i)
            }
        }
    }
}