package com.example.uas_project_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uas_project_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan Data Binding untuk mengaitkan layout dengan kode Kotlin
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menggantikan fragment utama dengan Home2Fragment pada saat penciptaan aktivitas
        replaceFragment(Home2Fragment())

        // Menetapkan listener untuk bottom navigation view
        binding.bottomnavigationview.setOnItemSelectedListener {
            when (it.itemId) {

                // Jika item "Home" dipilih, gantikan fragment dengan Home2Fragment
                R.id.home -> replaceFragment(Home2Fragment())

                // Jika item "Profile" dipilih, gantikan fragment dengan ProfileFragment
                R.id.profile-> replaceFragment(ProfileFragment())

            }
            // Memberikan sinyal bahwa event telah di-handle
            true
        }
    }

    // Fungsi untuk menggantikan fragment di dalam frame layout
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)// Menggantikan fragment yang ada di dalam frame_layout
            .commit()
    }
}
