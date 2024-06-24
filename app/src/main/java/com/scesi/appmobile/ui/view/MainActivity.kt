package com.scesi.appmobile.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scesi.appmobile.ui.viewmodel.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.databinding.ActivityMainBinding
import com.scesi.appmobile.network.RetrofitClient

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        movieAdapter = MovieAdapter()
        binding.recyclerView.adapter = movieAdapter

        // Initialize ViewModel
        val factory = MovieViewModelFactory(MovieRepository(RetrofitClient.apiService))
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.movies.observe(this, Observer { movies ->
            movieAdapter.submitList(movies)
        })

        // Add scroll listener for pagination
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem + 3 >= totalItemCount) {
                    viewModel.getNowPlayingMovies()
                }
            }
        })

        // Load initial data
        viewModel.getNowPlayingMovies()
    }
}