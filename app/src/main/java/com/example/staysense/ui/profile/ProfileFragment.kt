package com.example.staysense.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.staysense.databinding.FragmentProfileBinding
import com.example.staysense.ui.authentication.LoginActivity
import com.example.staysense.ui.welcomescreen.WelcomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth


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

        binding.btnLogout.setOnClickListener{setupLogout()}
    }

    private fun setupLogout() {
        Firebase.auth.signOut()
        val intent = Intent(requireContext(), WelcomeScreenActivity::class.java)
        startActivity(intent)

        Toast.makeText(requireContext(), "Logout Successfull", Toast.LENGTH_SHORT).show()
        requireActivity().finish()
    }
}