package com.example.staysense.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.staysense.databinding.FragmentProfileBinding
import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.example.staysense.R


class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

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

        // save username email
        val sharedPref = requireActivity().getSharedPreferences("StaySensePrefs", AppCompatActivity.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val email = sharedPref.getString("email", "")

        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)

        tvUsername.text = username
        tvEmail.text = email

        // logout
        binding.llLogout.setOnClickListener{setupLogout()}

        // navigate to other fragments
        binding.llPersonalInformationProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_personalInformationFragment)
        }
        binding.llPasswordSecurity.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_securityFragment)
        }
        binding.llAboutStaysense.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
    }

    private fun setupLogout() {
        Firebase.auth.signOut()
        val intent = Intent(requireContext(), WelcomeScreenActivity::class.java)
        startActivity(intent)

        Toast.makeText(requireContext(), "Logout Successfull", Toast.LENGTH_SHORT).show()
        requireActivity().finish()
    }
}