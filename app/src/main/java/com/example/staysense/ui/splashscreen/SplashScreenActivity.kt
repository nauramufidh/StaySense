package com.example.staysense.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.staysense.R
import com.example.staysense.database.UserSession
import com.example.staysense.ui.main.MainActivity
import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
        }, 2000)
    }

    private fun checkLogin() {
        if (UserSession.isLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, WelcomeScreenActivity::class.java))
        }
        finish()
    }

}