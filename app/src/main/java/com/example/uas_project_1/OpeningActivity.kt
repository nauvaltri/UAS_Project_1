package com.example.uas_project_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.uas_project_1.ViewPagerAdapter
import com.example.uas_project_1.R
import com.example.uas_project_1.databinding.ActivityOpeningBinding
import com.google.android.material.tabs.TabLayout



class OpeningActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOpeningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOpeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isAdminLoggedIn = sharedPref.getBoolean("isAdminLoggedIn", false)
        val isUserLoggedIn = sharedPref.getBoolean("isUserLoggedIn", false)


        with(binding) {
            openingLogo.alpha = 0f
            openingLogo.animate().setDuration(2000).alpha(1f).withEndAction() {
                val targetActivity = if (isAdminLoggedIn) {
                    AdminHomeMainActivity::class.java
                } else if (isUserLoggedIn){
                    MainActivity::class.java
                } else {
                    LoginRegister::class.java
                }
                val intent = Intent(this@OpeningActivity, targetActivity)
                startActivity(intent)
                finish()
            }
        }

    }
}

