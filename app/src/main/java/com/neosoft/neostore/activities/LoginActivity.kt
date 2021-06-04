package com.neosoft.neostore.activities

import CustomProgressDialog
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.models.Data
import com.neosoft.neostore.models.ForgotPModel
import com.neosoft.neostore.utilities.SessionManagement
import com.neosoft.neostore.utilities.Validations
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.email
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val ret = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    private var backPressTime = 0L
    lateinit var session: SessionManagement
    lateinit var email: String
    lateinit var password: String
    val progressDialog = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = SessionManagement(this)
        initialization()

    }

    //for button clicks
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onClick(view: View) {
        when (view.getId()) {
            //for forgot password
            R.id.txt_forgot_password -> {
                var email: String = ed_txt_username.text.toString()
                ret.forgotPassword(email).enqueue(object : Callback<ForgotPModel> {
                    override fun onResponse(
                        call: Call<ForgotPModel>,
                        response: Response<ForgotPModel>
                    ) {
                        try {
                            if (response.code() == 200) {
                                toast(response.body()?.user_msg.toString())
                            } else if (response.code() == 500) {
                                toast(response.body()?.user_msg.toString())
                            } else {
                                toast(response.message())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()

                        }
                    }


                    override fun onFailure(call: Call<ForgotPModel>, t: Throwable) {
                        toast(t.message.toString())
                    }

                })
            }

            //for register
            R.id.fab_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            //for login
            R.id.btn_login -> {
                if (validate()) {

                    email = ed_txt_username.text.toString()
                    password = edt_txt_password.text.toString()

                    ret.doLogin(email, password).enqueue(object : Callback<Data> {
                        override fun onResponse(call: Call<Data>, response: Response<Data>) {
                            progressDialog.show(this@LoginActivity, "Please wait...")
                            if (response.code() == 200) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progressDialog.dialog.dismiss()
                                }, 3000)

                                toast(response.body()?.message.toString())
                                session.createLoginSession(email, password)
                                val i: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(i)
                            } else if (response.code() == 500) {

                                Handler(Looper.getMainLooper()).postDelayed({
                                    progressDialog.dialog.dismiss()
                                    toast(response.message().toString())
                                }, 3000)


                            } else {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progressDialog.dialog.dismiss()
                                    toast(response.message().toString())
                                }, 3000)

                            }
                        }

                        override fun onFailure(call: Call<Data>, t: Throwable) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dialog.dismiss()
                                toast(t.message.toString())
                            }, 3000)


                        }

                    })
                }
            }
            else -> {
            }

        }

    }

    private fun initialization() {

        //for handling session
        if (session.isLoggedIn()) {
            var i: Intent = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        //for full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //initialization
        fab_register.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        txt_forgot_password.setOnClickListener(this)

    }

    //for back press
    override fun onBackPressed() {
        if (backPressTime + 1000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            toast(getString(R.string.press_back_again))
            backPressTime = System.currentTimeMillis()
        }

    }

    //validations for login
    private fun validate(): Boolean {
        email = ed_txt_username.text.toString()
        if (!Validations.isValidemail(email)) {
            ed_txt_username.error = getString(R.string.valid_email)
            return false

        } else if (edt_txt_password.text.toString().isEmpty()) {
            edt_txt_password.error = getString(R.string.canot_be_empty)
            return false

        }

        return true
    }


}
