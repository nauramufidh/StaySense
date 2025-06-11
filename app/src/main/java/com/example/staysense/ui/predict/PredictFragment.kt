package com.example.staysense.ui.predict

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import com.example.staysense.R
import com.example.staysense.databinding.FragmentPredictBinding
import com.example.staysense.ui.adapter.SectionPagerPredictAdapter
import com.example.staysense.ui.adapter.SectionsPagerDashboardAdapter
import com.example.staysense.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator


class PredictFragment : Fragment() {
    private var _binding : FragmentPredictBinding? = null
    private val binding get() = _binding!!

    companion object {
        @StringRes
        private val TAB_TITLES_PREDICT = intArrayOf(
            R.string.upload_file,
            R.string.input_data_manual
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionPagerPredictAdapter(requireActivity())
        binding.vpPredict.adapter = sectionsPagerAdapter
        binding.vpPredict.offscreenPageLimit = 2

        TabLayoutMediator(binding.tlPredict, binding.vpPredict) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_PREDICT[position])
        }.attach()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}