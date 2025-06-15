package com.example.staysense.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.staysense.R
import com.example.staysense.database.UserSession
import com.example.staysense.ui.main.MainActivity
import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val isFirstRun = UserSession.isFirstTimeRun(this)
            val isUserLoggedIn = UserSession.isLoggedIn(this)

            when {
                isFirstRun -> {
                    startActivity(Intent(this, WelcomeScreenActivity::class.java))
                }
                isUserLoggedIn -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, WelcomeScreenActivity::class.java))
                }
            }

            finish()
        }, 3000)
    }
}