package com.neosoft.neostore.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.RegisterationModel
import com.neosoft.neostore.utilities.LoadingDialog
import com.neosoft.neostore.utilities.Validations
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.time.ExperimentalTime

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    //initialize variables
    private val retIn: Api = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    private lateinit var gender: String
    private lateinit var firstname: String
    private lateinit var lastname: String
    lateinit var email: String
    lateinit var password: String
    private lateinit var confirmPassword: String
    lateinit var phone: String
    lateinit var loading: LoadingDialog
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialization()
        loading = LoadingDialog(this@RegisterActivity)
    }
    //for handling button clicks
    override fun onClick(view: View) {

        when (view.id) {
            //do registration api call
            R.id.btn_register -> {
                doRegistration()
            }
        }
    }
    //for initializing variables
    private fun initialization() {
        btn_register.setOnClickListener(this)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            gender = if (checkedId == R.id.radioButtonMale) {
                Constants.GENDER_MALE
            } else {
                Constants.GENDER_FEMALE
            }
        }
    }
    //for validating all fields
    private fun validate(): Boolean {
        email = edt_txt_email.text.toString()
        //for firstname
        if (edt_txt_first_name.text.toString().trim().isEmpty()) {
            edt_txt_first_name.error = getString(R.string.canot_be_empty)
            return false
        }
        //for last name
        else if (edt_txt_last_name.text.toString().trim().isEmpty()) {
            edt_txt_last_name.error = getString(R.string.canot_be_empty)
            return false
        }

        //for email
        else if (!Validations.isValidEmail(email)) {
            edt_txt_email.error = getString(R.string.valid_email)
            return false
        }

        //for password
        else if (edt_password.text.toString().trim().isEmpty() || edt_password.text.length < 6) {
            edt_password.error = getString(R.string.password_length)
            return false
        }
        //for confirm password
        else if (ed_txt_con_pass.text.toString().trim()
                .isEmpty() || ed_txt_con_pass.text.length < 6
        ) {
            ed_txt_con_pass.error = getString(R.string.password_length)
            return false
        }
        //for password matching
        else if (edt_password.text.toString() != ed_txt_con_pass.text.toString()) {
            toast(getString(R.string.pass_not_match))
            return false
        }
        //for radio button gender
        else if (radioGroup.checkedRadioButtonId == -1) {
            toast(getString(R.string.select_gender))
            return false
        }
        //for phone number
        else if (edt_txt_phone_no.text.length < 10) {
            edt_txt_phone_no.error = getString(R.string.valid_phone_no)
            return false
        }
        //for check terms and conditions
        else if (!checkbox_terms_cond.isChecked) {
            toast(getString(R.string.check_terms_cond))
            return false
        }
        return true
    }
    private fun doRegistration() {
        loading.startLoading()
        firstname = edt_txt_first_name.text.toString()
        lastname = edt_txt_last_name.text.toString()
        password = edt_password.text.toString()
        confirmPassword = ed_txt_con_pass.text.toString()
        phone = edt_txt_phone_no.text.toString()
        email = edt_txt_email.text.toString()
        if (validate())
        //call for  api
        {
            retIn.register(firstname, lastname, email, password, confirmPassword, gender, phone)
                .enqueue(object : Callback<RegisterationModel> {
                    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
                    override fun onResponse(call: Call<RegisterationModel>, response: Response<RegisterationModel>)
                    {
                        try {
                            when {
                                response.code() == Constants.SUCESS_CODE -> {
                                    val handler = Handler()
                                    handler.postDelayed({
                                        loading.isDismiss()
                                        toast(response.body()?.message.toString())
                                        val i= Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(i)
                                    }, Constants.DELAY_TIME.toLong())
                                }
                                response.code() == Constants.Error_CODE -> {
                                    loading.isDismiss()
                                    toast(response.body()?.message.toString())
                                }response.code() == Constants.INVALID_DATA->{
                                loading.isDismiss()
                                toast(response.body()?.message.toString())
                                }
                                else -> {
                                    loading.isDismiss()
                                    toast(response.body()?.message.toString())
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    override fun onFailure(call: Call<RegisterationModel>, t: Throwable) {
                        loading.isDismiss()
                        toast(R.string.no_connection)
                    }
                })
        }
    }
}