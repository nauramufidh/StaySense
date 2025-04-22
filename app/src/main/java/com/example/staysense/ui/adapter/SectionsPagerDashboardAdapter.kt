package com.example.staysense.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.staysense.ui.home.HomeFragment
import com.example.staysense.ui.home.RateFragment

class SectionsPagerDashboardAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> RateFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}