package com.example.staysense.ui.profile

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.staysense.databinding.FragmentProfileBinding

import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity

import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.example.staysense.R
import com.example.staysense.database.UserSession
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

//    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance("https://staysense-624b4-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = firebaseDatabase.reference.child("users")

        val username = UserSession.getUsername(requireContext())
        val email = UserSession.getEmail(requireContext())

        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)

        tvUsername.text = username
        tvEmail.text = email

        binding.llLogout.setOnClickListener { setupLogout() }

        binding.llPersonalInformationProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_personalInformationFragment)
        }
        binding.llPasswordSecurity.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_securityFragment)
        }
        binding.llAboutStaysense.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
        binding.llHistory.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_predictHistoryFragment)
        }
    }

//    private fun setupLogout() {
//
//        UserSession.clearUser(requireContext())
//
//        Firebase.auth.signOut()
//
//        val intent = Intent(requireContext(), WelcomeScreenActivity::class.java)
//        startActivity(intent)
//
//        Toast.makeText(requireContext(), "Logout Successfull", Toast.LENGTH_SHORT).show()
//        requireActivity().finish()
//    }

    private fun setupLogout(){
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage(getString(R.string.confirm_logout))
            .setPositiveButton("Logout"){_, _, ->
                val sharedPreferences =
                    requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    remove("CURRENT_USERNAME")
                    putBoolean("IS_LOGGED_IN", false)
                    apply()
                }

                val intent = Intent(requireContext(), WelcomeScreenActivity::class.java)
                startActivity(intent)

                Toast.makeText(requireContext(), "Logout Successfully", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.disagree)){dialog, _, ->
                dialog.dismiss()
            }
            .show()
    }
}