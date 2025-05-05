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

class SecurityFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("StaySensePrefs", AppCompatActivity.MODE_PRIVATE)
        val userID = sharedPref.getString("id", "")

        val etCurrentPassword = view.findViewById<EditText>(R.id.et_current_password_security)
        val etNewPassword = view.findViewById<EditText>(R.id.et_new_password_security)
        val etRetypeNewPassword = view.findViewById<EditText>(R.id.et_retype_new_password_security)
        val btnChangePassword = view.findViewById<Button>(R.id.btn_change_password_security)


        btnChangePassword.setOnClickListener {
          val currentPassword = etCurrentPassword.text.toString()
          val newPassword = etNewPassword.text.toString()
          val retypeNewPassword = etRetypeNewPassword.text.toString()

          if (newPassword != retypeNewPassword) {
              Toast.makeText(requireContext(), "New password and retype new password do not match", Toast.LENGTH_SHORT).show()
              return@setOnClickListener
          }
            if (userID != null) {
                val userRef = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(userID)
                userRef.get().addOnSuccessListener { snapshot ->
                    val savedPassword = snapshot.child("password").value.toString()

                    if (currentPassword != savedPassword) {
                        showToast("Current password is incorrect")
                    } else {
                        userRef.child("password").setValue(newPassword)
                        userRef.child("confirmPassword").setValue(newPassword)
                        showToast("Password updated successfully")
                    }
                }.addOnFailureListener {
                    showToast("Failed to fetch data")
                }
            } else {
                showToast("User not logged in")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}