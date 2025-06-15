package com.example.staysense.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.staysense.database.UserSession
import com.example.staysense.databinding.ActivityLoginBinding
import com.example.staysense.ui.main.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = firebaseDatabase.reference.child("users")

        binding.btnLogin2.setOnClickListener {
            val loginEmail = binding.etEmailLogin.text.toString()
            val loginPassword = binding.etPasswordLogin.text.toString()

            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()){
                setupLogin(loginEmail, loginPassword)
            }else{
                Toast.makeText(this@LoginActivity, "All Fields are Mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRedirectToSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
    }

    private fun setupLogin(email: String ,password: String){
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    for (userSnapshot in dataSnapshot.children){
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            UserSession.saveUser(
                                context = this@LoginActivity,
                                id = userData.id ?: "",
                                username = userData.username ?: "",
                                email = userData.email ?: "",
                                password = userData.password ?: ""
                            )

                            Toast.makeText(this@LoginActivity, "Login Successfull", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("username", userData.username)
                            intent.putExtra("email", userData.email)
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}