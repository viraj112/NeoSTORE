package com.neosoft.neostore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.PlaceOrderModel
import kotlinx.android.synthetic.main.activity_add_address.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressActivity : AppCompatActivity(), View.OnClickListener {
    val my_retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       supportActionBar?.title =getString(R.string.add_address)
        initialization()
    }

    private fun initialization() {
        btn_save_address.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id)
        {
            R.id.btn_save_address ->
            {
                saveAddress()
            }
        }
    }

    private fun saveAddress() {
            val i :Intent = Intent(this,AddressListActivity::class.java)
            i.putExtra("address",edittxt_address.text.toString()+edttxt_city.text.toString()+edttxt_landmark.text.toString()+edttxt_state.text.toString()+edttxt_country.text.toString()+edttxt_zip_code.text.toString())
            startActivity(i)
    }
}