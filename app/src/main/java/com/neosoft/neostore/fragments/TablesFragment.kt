package com.neosoft.neostore.fragments

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
import com.neosoft.neostore.adapters.TablesAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ProductList
import com.neosoft.neostore.models.ProductModel
import com.neosoft.neostore.utilities.LoadingDialog
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
    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_tables, container, false)
         loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        return view

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        rv_tables_fragment.layoutManager = LinearLayoutManager(activity)
        rv_tables_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        //get product list api call
        getProductList()
    }

    private fun getProductList()
    {
        my_retrofit.getProductList(1,10,1).enqueue(object :Callback<ProductList>{
            @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>)
            {
                try
                {
                    if (response.code()==Constants.SUCESS_CODE)
                    {
                        val handler = Handler()
                        handler.postDelayed(object : Runnable
                        {
                            override fun run()
                            {
                                loadingDialog.isDismiss()
                                val data:List<ProductModel> =response.body()?.data!!
                                listdata =data
                                //setRecycler list
                                setRecycler()
                            }
                        }, Constants.DELAY_TIME.toLong())

                    }else if (response.code() == Constants.NOT_FOUND)

                    {
                        loadingDialog.isDismiss()
                        activity?.toast(response.message().toString())
                    }

                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ProductList>, t: Throwable)
            {
                loadingDialog.isDismiss()
               activity?.toast(t.message.toString())
            }
        })
    }
        //set recycler view
    private fun setRecycler()
    {
        adapter = TablesAdapter(requireContext(),listdata)
        rv_tables_fragment?.adapter = adapter
        adapter.notifyDataSetChanged()

    }


}