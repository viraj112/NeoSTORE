package com.neosoft.neostore.fragments

import CustomProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlinx.android.synthetic.main.fragment_my_cart.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MyCartFragment : Fragment(), View.OnClickListener {
    val retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var adapter: MyCartAdapter
    var listdata: List<Data> = ArrayList()
    lateinit var token: String
    lateinit var total: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var count: String
    val progressDialog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()

        val view = inflater.inflate(R.layout.fragment_my_cart, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reecycler_my_cart.layoutManager = LinearLayoutManager(activity)
        reecycler_my_cart.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        btn_my_cart_order_now.visibility = View.GONE
        txt_total_my_cart.visibility = View.GONE
        txt_sum_my_cart.visibility = View.GONE
       // progressDialog.show(requireContext(),getString(R.string.please_wait))
        btn_my_cart_order_now.setOnClickListener(this)
        getCartList()

    }

    private fun getCartList() {
        retrofit.getCartList(token).enqueue(object : Callback<MyCartListModel> {

            override fun onResponse(
                call: Call<MyCartListModel>,
                response: Response<MyCartListModel>
            ) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {

                                listdata=response.body()?.data!!
                                total = response.body()?.total.toString()
                                count = response.body()?.count.toString()
                                setRecycler()
                                //for show visbility
                                visibility()



                    } else {
                             activity?.toast(response.message().toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<MyCartListModel>, t: Throwable) {

                activity?.toast(t.message.toString())
            }
        })
    }


    private fun setRecycler() {
        adapter = MyCartAdapter(requireActivity(), listdata)
        reecycler_my_cart.adapter = adapter
        adapter.notifyDataSetChanged()
        txt_sum_my_cart.text = total

    }


    private fun visibility() {
        btn_my_cart_order_now.visibility = View.VISIBLE
        txt_total_my_cart.visibility = View.VISIBLE
        txt_sum_my_cart.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        when(view.id)
        {
            R.id.btn_my_cart_order_now ->
            {
                val i:Intent = Intent(activity,AddAddressActivity::class.java)
                startActivity(i)
            }
        }

    }


}