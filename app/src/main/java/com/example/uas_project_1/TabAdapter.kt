package com.example.uas_project_1

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uas_project_1.LoginTabFragment
import com.example.uas_project_1.SignupTabFragment


// Adapter untuk mengelola fragment pada tab
class TabAdapter(act:AppCompatActivity) : FragmentStateAdapter(act) {

    // Mendefinisikan jumlah tab yang akan ditampilkan
    override fun getItemCount(): Int {
        return 2
    }

    // Membuat fragment sesuai dengan posisi tab
    override fun createFragment(position: Int): Fragment {
        return when(position) {

            // Jika posisi adalah 0, kembalikan instance LoginTabFragment
            0 -> LoginTabFragment()

            // Jika posisi adalah 1, kembalikan instance SignupTabFragment
            1 -> SignupTabFragment()

            // Jika posisi tidak valid, lemparkan IllegalArgumentException
            else -> throw IllegalArgumentException("Position out of array")
        }
    }


}