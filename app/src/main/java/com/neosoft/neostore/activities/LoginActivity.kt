package com.neosoft.neostore.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ForgotPModel
import com.neosoft.neostore.models.LoginModel
import com.neosoft.neostore.utilities.LoadingDialog
import com.neosoft.neostore.utilities.SessionManagement
import com.neosoft.neostore.utilities.Validations
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //variable declaration
    private val ret: Api = RetrofitClient.getRetrofitInstance().create(Api::class.java)
    private var backPressTime = 0L
    lateinit var session: SessionManagement
    lateinit var email: String
    lateinit var password: String
    lateinit var myEmail: String
    lateinit var username: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var loading: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loading = LoadingDialog(this@LoginActivity)
        session = SessionManagement(this)
        //initialization variables
        initialization()

    }

    //for managing  button clicks
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onClick(view: View) {

        when (view.id) {

            //for forgot password api call
            R.id.txt_forgot_password ->
                doForgotPassword()

            //for register pi call
            R.id.fab_register ->
                getRegistration()

            //for login api call
            R.id.btn_login ->
                if (validate())
                    doLogin()

            else -> {
                //do nothing
            }

        }

    }

    private fun doLogin() {
        email = ed_txt_username.text.toString()
        password = edt_txt_password.text.toString()
        ret.doLogin(email, password).enqueue(object : Callback<LoginModel> {
            @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                try {
                    when {
                        response.code() == Constants.SUCESS_CODE -> {
                            loading.startLoading()
                            val handler = Handler()
                            handler.postDelayed({
                                loading.isDismiss()
                                val items = response.body()?.data
                                myEmail = items?.email.toString()
                                username =
                                    items?.first_name.toString() + items?.last_name.toString()
                                val token = items?.access_token.toString()
                                val image = items?.profile_pic
                                toast(response.body()?.message.toString())

                                session.createLoginSession(email, password)

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("email", myEmail)
                                editor.putString("username", username)
                                editor.putString("token", token)
                                editor.putString("pic", image as String?)
                                editor.apply()
                                startActivity(intent)
                            }, Constants.DELAY_TIME.toLong())
                        }
                        response.code() == Constants.Error_CODE -> {
                            loading.isDismiss()
                            toast(response.message().toString())
                        }
                        else -> {
                            loading.isDismiss()
                            toast(response.message().toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                toast(R.string.no_connection)
            }
        })

    }

    //registration
    private fun getRegistration() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    //forgot password api call
    private fun doForgotPassword() {
        val email: String = ed_txt_username.text.toString()
        ret.forgotPassword(email).enqueue(object : Callback<ForgotPModel> {
            override fun onResponse(call: Call<ForgotPModel>, response: Response<ForgotPModel>) {
                try {
                    when {
                        response.code() == Constants.SUCESS_CODE -> {

                            toast(response.body()?.user_msg.toString())
                        }
                        response.code() == Constants.Error_CODE -> {
                            toast(response.body()?.user_msg.toString())
                        }
                        else -> {
                            toast(response.message())
                        }
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

    private fun initialization() {

        //for handling session
        if (session.isLoggedIn()) {
            val i = Intent(this, MainActivity::class.java)
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

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
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
        if (!Validations.isValidEmail(email)) {
            ed_txt_username.error = getString(R.string.valid_email)
            return false

        } else if (edt_txt_password.text.toString().isEmpty()) {
            edt_txt_password.error = getString(R.string.canot_be_empty)
            return false

        }
        return true
    }
}
