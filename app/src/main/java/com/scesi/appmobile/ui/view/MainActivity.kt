package com.scesi.appmobile.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.ActivityMainBinding
import android.util.Log
import android.view.Window
import android.view.WindowManager
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE)
        val isFirstInitialization = sharedPreferences.getBoolean("is_first_initialization", true)

        if (isFirstInitialization) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("onboarding_complete", false)
            editor.putBoolean("is_first_initialization", false)
            editor.apply()
            Log.d("MainActivity", "onboarding_complete reset to false for the first initialization")
        }

        val onboardingComplete = sharedPreferences.getBoolean("onboarding_complete", false)

        if (!onboardingComplete) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }
//        val window: Window = window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = resources.getColor(R.color.rojo_200)
//        window.navigationBarColor = resources.getColor(R.color.rojo_200)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_favorites -> {
                    navController.navigate(R.id.favoritesFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}
