package com.neosoft.neostore.activities

import CustomProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.models.RegisterationModel
import com.neosoft.neostore.utilities.Validations
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.time.ExperimentalTime

class RegisterActivity : AppCompatActivity(),View.OnClickListener{
    val retIn = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    val progressDialog = CustomProgressDialog()
    lateinit var Gender :String
    lateinit var first_name: String
    lateinit var last_name: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirm_password: String
    lateinit var phone: String
    //lateinit var gender: String


    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //for  custom toolbar
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.register)
        toolbar.setNavigationOnClickListener {
            var intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        initialization()
    }





    override fun onClick(view: View){

        when(view.id){

            //do registration
            R.id.btn_register -> {
                first_name = edt_txt_first_name.text.toString()
                last_name = edt_txt_last_name.text.toString()
                password = edt_password.text.toString()
                confirm_password = ed_txt_con_pass.text.toString()
                phone = edt_txt_phone_no.text.toString()
                email = edt_txt_email.text.toString()


                if (validate()){
                    retIn.register(first_name, last_name, email, password, confirm_password, Gender, phone)
                        .enqueue(object : Callback<RegisterationModel> {
                            override fun onResponse(
                                call: Call<RegisterationModel>,
                                response: Response<RegisterationModel>) {
                                try {
                                    if (response.code() == 200) {
                                        toast(response.body()?.message.toString())
                                    } else if (response.code() == 500) {
                                        toast(response.message())
                                    } else {
                                        toast(response.body()?.message.toString())
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(call: Call<RegisterationModel>, t: Throwable) {
                                toast(t.message.toString())

                            }
                        })
                }
            }

            }
        }

    //for initializing varables
    private fun initialization() {

        btn_register.setOnClickListener(this)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId ==R.id.radioButtonMale)
            {
                Gender = "M"
            }else{
                    Gender = "F"
            }
        }
    }


    //for validating all fields
    private fun validate(): Boolean {
        email = edt_txt_email.text.toString()

        //for firstname
        if(edt_txt_first_name.text.toString().trim().isEmpty())
        {
            edt_txt_first_name.error =getString(R.string.canot_be_empty)
            return false
        }

        //for last name
        else if(edt_txt_last_name.text.toString().trim().isEmpty())
        {
            edt_txt_last_name.error =getString(R.string.canot_be_empty)
            return false
        }

        //for email
        else if (!Validations.isValidemail(email)) {
            edt_txt_email.error = getString(R.string.valid_email)
            return false
        }

        //for password
        else if (edt_password.text.toString().trim().isEmpty() ||edt_password.text.length<6)
        {
            edt_password.error =getString(R.string.password_length)
            return false
        }
         //for confirm password
        else if (ed_txt_con_pass.text.toString().trim().isEmpty() ||ed_txt_con_pass.text.length<6)
        {
            ed_txt_con_pass.error =getString(R.string.password_length)
            return false
        }
            //for password matching
        else if(!edt_password.text.toString().equals(ed_txt_con_pass.text.toString())){
            toast(getString(R.string.pass_not_match))
            return false
        }

        //for radio button gender
        else if(radioGroup.checkedRadioButtonId ==-1)
        {
            toast("please select gender")
            return false
        }
        //for phone number
        else if (edt_txt_phone_no.text.length<10)
        {
            edt_txt_phone_no.error=getString(R.string.valid_phone_no)
            return false
        }
        //for check terms and conditions
        else if (!checkbox_terms_cond.isChecked){
            toast(getString(R.string.check_terms_cond))
            return false
        }

            return true
    }
}