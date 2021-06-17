package com.neosoft.neostore.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.neosoft.neostore.R
import com.neosoft.neostore.adapters.AddressAdapter
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.database.AddressDatabase
import com.neosoft.neostore.database.AddressEntity
import com.neosoft.neostore.models.PlaceOrderModel
import kotlinx.android.synthetic.main.activity_address_list.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : AppCompatActivity(), View.OnClickListener,AddressAdapter.OnItemClick{
    lateinit var sharedPreferences: SharedPreferences
    lateinit var  Name:String
    lateinit var Address:String
    var add:String=""
    var addressAdapter :AddressAdapter? =null
    val onItemClick:AddressAdapter.OnItemClick? = null
    val retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title =getString(R.string.address_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!

        Address = intent.getStringExtra("address").toString()
        Name = sharedPreferences.getString("username",null).toString()

        setContentView(R.layout.activity_address_list)

        btn_place_order.setOnClickListener(this)
        val database = Room.databaseBuilder(
            this,
               AddressDatabase::class.java,
            "addressdatabase"
        )
            .allowMainThreadQueries()
            .build()

        database.addressDao().insertAddress(AddressEntity(name=Name,address =Address))
        val allAddress= database.addressDao().getAddress()
        recycler_address_list.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = AddressAdapter(this@AddressListActivity,allAddress,this@AddressListActivity)

        }

    }


    override fun onClick(myadd: String) {
        add = myadd
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_address_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.menu_add_address ->
            {
                val i:Intent = Intent(this,AddAddressActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when(view.id)
        {
            R.id.btn_place_order ->
                placeOrder()
            }
        }

    private fun placeOrder() {

        retrofit.placeOrder(Constants.TOKEN,add).enqueue(object :Callback<PlaceOrderModel>{
            override fun onResponse(
                call: Call<PlaceOrderModel>,
                response: Response<PlaceOrderModel>
            ) {
                try {
                    if (response.code()==Constants.SUCESS_CODE)
                    {
                        toast(response.body()?.user_msg.toString())

                    }else if (response.code()==Constants.NOT_FOUND)
                    {
                        toast(response.body()?.message.toString())
                    }

                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<PlaceOrderModel>, t: Throwable) {
                toast(t.message.toString())
            }
        })
    }


}




