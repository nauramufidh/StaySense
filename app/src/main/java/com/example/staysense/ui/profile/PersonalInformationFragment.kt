package com.example.staysense.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.staysense.R
import com.google.firebase.database.FirebaseDatabase

class PersonalInformationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("StaySensePrefs", AppCompatActivity.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val email = sharedPref.getString("email", "")
        val userID = sharedPref.getString("id", "")

        val etUsername = view.findViewById<EditText>(R.id.et_username_personal_information)
        val etEmail = view.findViewById<EditText>(R.id.et_email_personal_information)

        val etFullName = view.findViewById<EditText>(R.id.et_full_name_personal_information)
        val etRole = view.findViewById<EditText>(R.id.et_role_personal_information)
        val btnSave = view.findViewById<Button>(R.id.btn_save_personal_information)

        etUsername.setText(username)
        etEmail.setText(email)

        if (userID != null) {
            val database = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app")
            val userRef = database.getReference("users").child(userID)

            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val username = snapshot.child("username").getValue(String::class.java)
                    val fullName = snapshot.child("full_name").getValue(String::class.java)
                    val role = snapshot.child("role").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)

                    etUsername.setText(username ?: "")
                    etFullName.setText(fullName ?: "")
                    etRole.setText(role ?: "")
                    etEmail.setText(email ?: "")
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if (userID != null) {
                val database = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app")
                val userRef = database.getReference("users").child(userID)

                val newUsername = etUsername.text.toString()
                val newEmail = etEmail.text.toString()

                val updates = mapOf<String, Any>(
                    "full_name" to etFullName.text.toString(),
                    "role" to etRole.text.toString(),
                    "username" to newUsername,
                    "email" to newEmail
                )

                userRef.updateChildren(updates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val sharedPref = requireActivity().getSharedPreferences("StaySensePrefs", AppCompatActivity.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("username", newUsername)
                            putString("email", newEmail)
                            apply()
                        }

                        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}