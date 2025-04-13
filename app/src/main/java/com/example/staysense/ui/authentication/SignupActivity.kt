package com.example.staysense.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.staysense.R
import com.example.staysense.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()
            val confirmPassword = binding.etConfirmPasswordSignup.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else{
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvRedirectToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}