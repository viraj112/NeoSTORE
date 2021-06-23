package com.neosoft.neostore.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import kotlinx.android.synthetic.main.activity_add_address.*
import org.jetbrains.anko.toast

class AddAddressActivity : AppCompatActivity(),View.OnClickListener {
    //initialize variables
    lateinit var  name:String
    lateinit var sharedPreferences: SharedPreferences

     override fun onCreate(savedInstanceState: Bundle?)
     {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_address)
        sharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        name = sharedPreferences.getString("username", null).toString()

         //initialize variables
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
                if(validation())
                {
                    val i :Intent = Intent(this,AddressListActivity::class.java)
                    i.putExtra("address",edittxt_address.text.toString()+edttxt_landmark.text.toString()+edttxt_city.text.toString()+edttxt_state.text.toString()+edttxt_zip_code.text.toString())
                    startActivity(i)

                }

            }
        }
    }
    //validation for fields
    private fun validation() :Boolean

    {
        if (edittxt_address.text.toString().isEmpty())
        {
            edittxt_address.error = getString(R.string.canot_be_empty)
            return false
        }else if (edttxt_landmark.text.toString().isEmpty())
        {
            edttxt_landmark.error =getString(R.string.canot_be_empty)
            return false
        }
        else if (edttxt_city.text.toString().isEmpty())
        {
            edttxt_city.error =getString(R.string.canot_be_empty)
            return false
        }else if (edttxt_city.text.toString().isEmpty())
        {
            edttxt_city.error =getString(R.string.canot_be_empty)
            return false
        }else if ((edttxt_state.text.toString().isEmpty()))
        {
            edttxt_state.error =getString(R.string.canot_be_empty)
            return false
        }else if (edttxt_zip_code.text.toString().isEmpty())
        {
            edttxt_zip_code.error = getString(R.string.valid_zip)
            return false
        }else if(edttxt_zip_code.text.toString().length<6)
        {
            edttxt_zip_code.error = getString(R.string.valid_zip)
            return false

        }
            return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}





