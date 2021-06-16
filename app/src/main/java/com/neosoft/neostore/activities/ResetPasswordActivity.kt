package com.neosoft.neostore.activities

import CustomProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ChangePasswordModel
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    val retrofit = RetrofitClient.getRetrofitInstance().create(Api::class.java)

    lateinit var old_password: String
    lateinit var new_password: String
    lateinit var confirm_password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        supportActionBar?.title = getString(R.string.reset_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initialization()
    }

    private fun initialization() {
        btn_change_password.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onClick(view: View) {
        when (view.id) {
            //click for change password
            R.id.btn_change_password ->
            {
                if (validate()) {

                    old_password = edt_current_password.text.toString()
                    new_password = edt_new_password.text.toString()
                    confirm_password = edt_confim_password.text.toString()

                    changePassword()

                }
            }
        }
    }

    //api call for change password
    private fun changePassword() {
        retrofit.changePassword(Constants.TOKEN, old_password, new_password, confirm_password)
            .enqueue(object : Callback<ChangePasswordModel> {
                @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
                override fun onResponse(
                    call: Call<ChangePasswordModel>,
                    response: Response<ChangePasswordModel>
                ) {

                    try {
                        if (response.code() == Constants.SUCESS_CODE) {
                                toast(response.body()?.user_msg.toString())


                        } else if (response.code() == Constants.DATA_MISSING) {
                                toast(response.body()?.user_msg.toString())

                        } else {

                                toast(response.body()?.user_msg.toString())

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ChangePasswordModel>, t: Throwable) {
                        toast(t.message.toString())
                        clearData()


                }
            })
    }

    private fun clearData() {
        edt_current_password.setText("")
        edt_new_password.setText("")
        edt_confim_password.setText("")
    }

    private fun validate(): Boolean {
        if (edt_current_password.text.toString()
                .equals("") || edt_current_password.text.toString().length < 6
        ) {
            edt_current_password.error = getString(R.string.password_length)
            return false
        } else if (edt_new_password.text.toString()
                .equals("") || edt_new_password.text.toString().length < 6
        ) {
            edt_new_password.error = getString(R.string.password_length)

        } else if (edt_confim_password.text.toString()
                .equals("") || edt_confim_password.text.toString().length < 6
        ) {
            edt_confim_password.error = getString(R.string.password_length)
        } else if (!edt_confim_password.text.toString().equals(edt_new_password.text.toString())) {
            toast(getString(R.string.pass_not_match))
        }
        return true

    }
}