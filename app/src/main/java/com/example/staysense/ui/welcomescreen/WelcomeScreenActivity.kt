package com.example.staysense.ui.welcomescreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.staysense.R
import com.example.staysense.databinding.ActivityWelcomeScreenBinding
import com.example.staysense.ui.authentication.LoginActivity
import com.example.staysense.ui.authentication.SignupActivity
import com.example.staysense.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.handleCoroutineException

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        setupaction()
        playAnimation()

//        Handler(Looper.getMainLooper()).postDelayed({
//            checkLogin()
//        }, 200)

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

        binding.btnLgsgmasuk.setOnClickListener {
            val intentMasuk = Intent(this, MainActivity::class.java)
            startActivity(intentMasuk)
            finish()
        }
    }

//    private fun checkLogin(){
//        val currentUser = firebaseAuth.currentUser
//        if (currentUser != null){
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//
//    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcomeIcon, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val getstarted = ObjectAnimator.ofFloat(binding.tvGetStarted, View.ALPHA, 1f).setDuration(500)
        val welcometxt = ObjectAnimator.ofFloat(binding.tvWelcomeText, View.ALPHA, 1f).setDuration(500)
        val btnsignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val haveacc = ObjectAnimator.ofFloat(binding.llHaveAcc, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(getstarted, welcometxt, btnsignup, haveacc)
            start()
        }

    }
}