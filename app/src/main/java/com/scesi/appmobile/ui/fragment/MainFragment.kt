package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.favoritesFragment),
            binding.drawerLayout
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
            if (handled) {
                binding.drawerLayout.closeDrawers()
            }
            handled
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleOnBackPressed(): Boolean {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
