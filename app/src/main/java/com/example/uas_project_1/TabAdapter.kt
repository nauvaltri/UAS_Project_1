package com.example.uas_project_1

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uas_project_1.LoginTabFragment
import com.example.uas_project_1.SignupTabFragment

class TabAdapter(act:AppCompatActivity) : FragmentStateAdapter(act) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> LoginTabFragment()
            1 -> SignupTabFragment()
            else -> throw IllegalArgumentException("Position out of array")
        }
    }


}