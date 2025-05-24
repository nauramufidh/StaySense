package com.example.staysense.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.staysense.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = firebaseDatabase.reference.child("users")

        binding.btnSignup.setOnClickListener {
            Log.d("SignupDebug", "Signup button clicked")
            val signupUsername = binding.etUsernameSignup.text.toString()
            val signupEmail = binding.cvEmailSignup.text.toString()
            val signupPassword = binding.cvPasswordSignup.text.toString()
            val signupConfirmPassword = binding.cvConfirmPasswordSignup.text.toString()

            if (signupUsername.isNotEmpty() && signupEmail.isNotEmpty() && signupPassword.isNotEmpty() && signupConfirmPassword.isNotEmpty()) {
                setupSignup(signupUsername, signupEmail, signupPassword, signupConfirmPassword)
            }else{
                Toast.makeText(this@SignupActivity, "All Fields are Mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cvPasswordSignup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.cvConfirmPasswordSignup.setOriginalPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tvRedirectToLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }

    }

    private fun setupSignup(username: String, email: String, password: String, confirmPassword: String) {

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@SignupActivity, "Email already registered!", Toast.LENGTH_SHORT).show()
                } else {
                    databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(usernameSnapshot: DataSnapshot) {
                            if (!usernameSnapshot.exists()) {
                                val id = databaseReference.push().key
                                val userData = UserData(id, username, email, password, confirmPassword)
                                databaseReference.child(id!!).setValue(userData)
                                Toast.makeText(this@SignupActivity, "Signup Successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@SignupActivity, "Username already exists!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignupActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}