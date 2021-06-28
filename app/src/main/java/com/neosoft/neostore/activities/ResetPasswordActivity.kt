package com.neosoft.neostore.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.TextKeyListener.clear
import android.view.View
import androidx.annotation.RequiresApi
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ChangePasswordModel
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    //initialize variables
    val retrofit: Api = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    private lateinit var oldPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String
    lateinit var loadingDialog: LoadingDialog
    private lateinit var preferences: SharedPreferences
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar?.title = getString(R.string.reset_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadingDialog = LoadingDialog(this)
        //assign value to variables
        initialization()
    }
    private fun initialization() {
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        token = preferences.getString("token", null).toString()
        btn_change_password.setOnClickListener(this)
    }
    //handle button clicks
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onClick(view: View) {
        when (view.id) {
            //click for change password
            R.id.btn_change_password -> {
                if (validate()) {
                    oldPassword = edt_current_password.text.toString()
                    newPassword = edt_new_password.text.toString()
                    confirmPassword = edt_confim_password.text.toString()
                    changePassword()
                }else
                {
                }
            }
        }
    }
    //api call for change password
    private fun changePassword() {
        loadingDialog.startLoading()
        retrofit.changePassword(token, oldPassword, newPassword, confirmPassword)
            .enqueue(object : Callback<ChangePasswordModel> {
                @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
                override fun onResponse(call: Call<ChangePasswordModel>, response: Response<ChangePasswordModel>) {
                    try {
                        when {
                            response.code() == Constants.SUCESS_CODE -> {
                                @Suppress("DEPRECATION") val handler = Handler()
                                handler.postDelayed({
                                    loadingDialog.isDismiss()
                                    toast(response.body()?.user_msg.toString())
                                    clearData()
                                }, Constants.DELAY_TIME.toLong())
                            }
                            response.code() == Constants.DATA_MISSING -> {
                                loadingDialog.isDismiss()
                                toast(response.body()?.user_msg.toString())
                            }
                            else -> {
                                loadingDialog.isDismiss()
                                toast(response.body()?.user_msg.toString())
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<ChangePasswordModel>, t: Throwable) {
                    loadingDialog.isDismiss()
                    toast(getString(R.string.no_connection))
                    clearData()
                }
            })
    }
    //clear fields
    private fun clearData() {
        edt_current_password.setText("")
        edt_new_password.setText("")
        edt_confim_password.setText("")
    }
    //validation for fields
    private fun validate(): Boolean {
        if (edt_current_password.text.toString() == "" || edt_current_password.text.toString().length < 6) {
            edt_current_password.error = getString(R.string.password_length)
            return false
        } else if (edt_new_password.text.toString() == "" || edt_new_password.text.toString().length < 6) {
            edt_new_password.error = getString(R.string.password_length)
            return false
        } else if (edt_confim_password.text.toString() == "" || edt_confim_password.text.toString().length < 6) {
            edt_confim_password.error = getString(R.string.password_length)
            return false
        } else if (edt_confim_password.text.toString() != edt_new_password.text.toString()) {
            toast(getString(R.string.pass_not_match))
            return false
        }
        return true
    }
    override fun onBackPressed() {
       finish()
        super.onBackPressed()
    }
}