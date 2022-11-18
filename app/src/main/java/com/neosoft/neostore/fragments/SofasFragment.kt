package com.neosoft.neostore.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
import com.neosoft.neostore.adapters.SofasAdapter
import com.neosoft.neostore.adapters.TablesAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ProductModel
import com.neosoft.neostore.models.SofaListModel
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.fragment_sofas.*
import kotlinx.android.synthetic.main.fragment_tables.*
import okhttp3.Callback
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response

class SofasFragment : Fragment() {
    lateinit var adapter: SofasAdapter
    var pageNumber :Int =1
    var listData: ArrayList<SofaListModel.SofaModel> = ArrayList()
    var displayList: ArrayList<SofaListModel.SofaModel> = ArrayList()
    private val myRetrofit: Api = RetrofitClientProduct.getRetrofitInstance().create(Api::class.java)
    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sofas, container, false)
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        setHasOptionsMenu(true)
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_sofas_fragment.layoutManager = LinearLayoutManager(activity)
        rv_sofas_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        getSofasList()
    }

    private fun getSofasList() {
        myRetrofit.getSofasList(3,10,pageNumber).enqueue(object :retrofit2.Callback<SofaListModel>{
            override fun onResponse(call: Call<SofaListModel>, response: Response<SofaListModel>) {
                try {
                    if (response.code() ==Constants.SUCESS_CODE){
                        val handler = Handler()
                        handler.postDelayed({
                           // pageNumber++
                            loadingDialog.isDismiss()
                            listData = response.body()?.data!!
                            displayList.addAll(listData)
                            setRecycler()
                            pageNumber++
                            getSofasList()

                        },Constants.DELAY_TIME.toLong())
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<SofaListModel>, t: Throwable) {
                loadingDialog.isDismiss()
                activity?.toast(getString(R.string.no_connection))

            }

        })

    }

    private fun setRecycler() {
        adapter = SofasAdapter(requireContext(), displayList)
        rv_sofas_fragment?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = getString(R.string.sofas)
    }
}