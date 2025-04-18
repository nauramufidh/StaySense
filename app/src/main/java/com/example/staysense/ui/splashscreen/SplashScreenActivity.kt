package com.example.staysense.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.staysense.R
import com.example.staysense.ui.main.MainActivity
import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, WelcomeScreenActivity::class.java))
        }, 3000)
    }


}