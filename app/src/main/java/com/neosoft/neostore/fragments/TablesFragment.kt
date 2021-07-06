@file:Suppress("DEPRECATION")

package com.neosoft.neostore.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
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
import java.util.*
import kotlin.collections.ArrayList

class TablesFragment : Fragment() {
    //initialize variables
    lateinit var adapter: TablesAdapter
    var listData: ArrayList<ProductModel> = ArrayList()
    var displayList: ArrayList<ProductModel> = ArrayList()
    private val myRetrofit: Api = RetrofitClientProduct.getRetrofitInstance().create(Api::class.java)
    lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tables, container, false)
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        setHasOptionsMenu(true)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_tables_fragment.layoutManager = LinearLayoutManager(activity)
        rv_tables_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        //get product list api call
        getProductList()
    }

    private fun getProductList() {
        myRetrofit.getProductList(1, 10, 1).enqueue(object : Callback<ProductList> {
            @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                try {
                    if (response.code() == Constants.SUCESS_CODE) {
                        val handler = Handler()
                        handler.postDelayed({
                            loadingDialog.isDismiss()
                            listData = response.body()?.data!!
                            displayList.addAll(listData)
                            //setRecycler list
                            setRecycler()
                        }, Constants.DELAY_TIME.toLong())
                    } else if (response.code() == Constants.NOT_FOUND) {
                        loadingDialog.isDismiss()
                        activity?.toast(response.message().toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<ProductList>, t: Throwable) {
                loadingDialog.isDismiss()
                activity?.toast(getString(R.string.no_connection))
            }
        })
    }
    //set recycler view
    private fun setRecycler() {
        adapter = TablesAdapter(requireContext(), displayList)
        rv_tables_fragment?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = getString(R.string.search_here)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        listData.forEach {
                            if (it.name.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(it)
                            }
                        }
                        rv_tables_fragment.adapter!!.notifyDataSetChanged()
                    } else {
                        displayList.clear()
                        displayList.addAll(listData)
                        rv_tables_fragment.adapter!!.notifyDataSetChanged()
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
        actionBar?.title = getString(R.string.tables)
    }
}