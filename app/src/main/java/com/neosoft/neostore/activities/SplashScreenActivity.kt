package com.neosoft.neostore.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.neosoft.neostore.R
import com.neosoft.neostore.utilities.SessionManagement
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sessionManagement: SessionManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    sessionManagement =SessionManagement(this)
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

        splsh_text.animate().setDuration(1000).alpha(1f).withEndAction {

               if (sessionManagement.isLoggedIn()){
                   val i = Intent(this, MainActivity::class.java)
                   startActivity(i)

               }else
               {
                   val i = Intent(this, LoginActivity::class.java)
                   startActivity(i)

               }
            overridePendingTransition(
                android.R.anim.bounce_interpolator,
                android.R.anim.bounce_interpolator
            )
            finish()
        }
    }
}