package com.neosoft.neostore.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neosoft.neostore.R
import com.neosoft.neostore.utilities.SessionManagement
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var session :SessionManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManagement(this)

        session.checkLogin()
        btn_logout.setOnClickListener {
            session.logoutUser()
        }
    }
}