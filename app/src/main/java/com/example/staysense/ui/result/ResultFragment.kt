package com.example.staysense.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.staysense.R
import com.example.staysense.databinding.FragmentResultBinding
import com.example.staysense.ui.main.MainActivity


class ResultFragment : Fragment() {
    private var _binding : FragmentResultBinding ? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupUploadResult()

    }

//    private fun setupUploadResult(){
//        val args = arguments
////        binding.tvMessageResult.text = args?.getString("messageresult")
//        binding.tvTotalcustResult.text = args?.getInt("total_customers").toString()
//        binding.tvTotCustChurnResult.text = args?.getInt("churn_count").toString()
//        binding.tvTotCustNotChurnResult.text = args?.getInt("not_churn_count").toString()
//        binding.tvChurnrateResult.text = args?.getString("churn_rate")
//
//        binding.btnOk.setOnClickListener {
//            findNavController().popBackStack(R.id.navigation_home, false)
//        }
//    }


}