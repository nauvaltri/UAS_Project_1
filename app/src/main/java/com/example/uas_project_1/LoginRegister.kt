package com.example.uas_project_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.uas_project_1.databinding.ActivityLoginRegisterBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LoginRegister : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Inisialisasi binding untuk layout activity_login_register.xml
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi TabLayout dan ViewPager dari layout
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)


        // Mendapatkan indeks tab yang dipilih dari intent
        val selectedTab = intent.getIntExtra("SELECTED_TAB", 0)
        with(binding) {

            // Mengatur adapter untuk ViewPager menggunakan TabAdapter
            viewPager.adapter = TabAdapter(this@LoginRegister)

            // Menyambungkan TabLayout dan ViewPager menggunakan TabLayoutMediator
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->

                // Mengatur teks tab berdasarkan posisi
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> ""
                }
            }.attach()

            // Mengatur ViewPager untuk menampilkan tab yang dipilih
            viewPager.setCurrentItem(selectedTab, true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_login -> {

                // Menangani aksi saat menu Login dipilih
                Toast.makeText(this@LoginRegister,"Login", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_register -> {

                // Menangani aksi saat menu Register dipilih
                Toast.makeText(this@LoginRegister,"Register", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}