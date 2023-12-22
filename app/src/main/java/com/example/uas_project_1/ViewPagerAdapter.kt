package com.example.uas_project_1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uas_project_1.LoginTabFragment
import com.example.uas_project_1.SignupTabFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            SignupTabFragment()
        } else LoginTabFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}