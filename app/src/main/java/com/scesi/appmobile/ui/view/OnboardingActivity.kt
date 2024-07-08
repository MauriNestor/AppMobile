package com.scesi.appmobile.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OnboardingAdapter()
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem + 1 < adapter.itemCount) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("onboarding_complete", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
