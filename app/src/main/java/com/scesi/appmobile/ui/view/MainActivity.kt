package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.ActivityMainBinding
import com.scesi.appmobile.ui.fragment.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment())
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container) as? MainFragment
        return fragment?.handleOnBackPressed() ?: false
    }
}
