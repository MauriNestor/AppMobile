package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.scesi.appmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf(
            MovieListFragment.newInstance("now_playing"),
            MovieListFragment.newInstance("popular"),
            MovieListFragment.newInstance("upcoming"),
            MovieListFragment.newInstance("top_rated")
        )

        val titles = listOf("Now Playing", "Popular", "Upcoming", "Top Rated")

        val adapter = ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}
