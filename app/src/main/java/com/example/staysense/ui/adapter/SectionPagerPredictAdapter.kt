package com.example.staysense.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.staysense.ui.predict.InputManualFragment
import com.example.staysense.ui.predict.UploadFileFragment

class SectionPagerPredictAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UploadFileFragment()
            1 -> InputManualFragment()
            else -> UploadFileFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}