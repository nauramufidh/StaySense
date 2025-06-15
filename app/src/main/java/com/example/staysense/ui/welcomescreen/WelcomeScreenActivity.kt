package com.example.staysense.ui.welcomescreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.staysense.databinding.ActivityWelcomeScreenBinding
import com.example.staysense.ui.authentication.LoginActivity
import com.example.staysense.ui.authentication.SignupActivity


class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupaction()
        playAnimation()

    }

    private fun setupaction() {
        binding.btnSignup.setOnClickListener {
            val intentToSignup = Intent(this, SignupActivity::class.java)
            startActivity(intentToSignup)
            finish()
        }

        binding.tvLoginToLogin.setOnClickListener {
            val intentToLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentToLogin)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcomeIcon, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcometxt = ObjectAnimator.ofFloat(binding.tvWelcomeText, View.ALPHA, 1f).setDuration(500)
        val btnsignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val haveacc = ObjectAnimator.ofFloat(binding.llHaveAcc, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(welcometxt, btnsignup, haveacc)
            start()
        }
    }
}