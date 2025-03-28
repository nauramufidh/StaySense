package com.example.staysense.ui.welcomescreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.staysense.R
import com.example.staysense.databinding.ActivityWelcomeScreenBinding
import com.example.staysense.ui.authentication.LoginActivity
import com.example.staysense.ui.authentication.SignupActivity

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupaction()
        playAnimation()

    }

    private fun setupaction() {
        binding.btnSignup.setOnClickListener {
            val intentToSignup = Intent(this, SignupActivity::class.java)
//            intentToSignup.putExtra("fromWelcome", true)
            startActivity(intentToSignup)
            finish()
        }

        binding.tvLoginToLogin.setOnClickListener {
            val intentToLogin = Intent(this, LoginActivity::class.java)
//            intentToLogin.putExtra("fromWelcome", true)
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

        val getstarted = ObjectAnimator.ofFloat(binding.tvGetStarted, View.ALPHA, 1f).setDuration(300)
        val welcometxt = ObjectAnimator.ofFloat(binding.tvWelcomeText, View.ALPHA, 1f).setDuration(300)
        val btnsignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(300)
        val haveacc = ObjectAnimator.ofFloat(binding.llHaveAcc, View.ALPHA, 1f).setDuration(300)


        AnimatorSet().apply {
            playSequentially(getstarted, welcometxt, btnsignup, haveacc)
            start()
        }

    }
}