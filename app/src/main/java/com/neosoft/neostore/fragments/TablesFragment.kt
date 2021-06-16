package com.neosoft.neostore.fragments

import CustomProgressDialog
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
import com.neosoft.neostore.adapters.TablesAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ProductList
import com.neosoft.neostore.models.ProductModel
import kotlinx.android.synthetic.main.fragment_tables.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class TablesFragment : Fragment() {

    lateinit var adapter: TablesAdapter
    var listdata:List<ProductModel> = ArrayList()
    val my_retrofit = RetrofitClientProduct.getRetrofitInstance().create(Api::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tables, container, false)

        return view

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_tables_fragment.layoutManager = LinearLayoutManager(activity)
        rv_tables_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))


        getProductList()
    }

    private fun getProductList() {

        my_retrofit.getProductList(1,10,1).enqueue(object :Callback<ProductList>{
            @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {

                try {
                    if (response.code()==Constants.SUCESS_CODE)
                    {
                            val data:List<ProductModel> =response.body()?.data!!
                            listdata =data
                            adapter = TablesAdapter(context!!,listdata)
                            rv_tables_fragment?.adapter = adapter
                            adapter.notifyDataSetChanged()


                    }

                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ProductList>, t: Throwable) {

               activity?.toast(t.message.toString())
            }
        })
    }



}