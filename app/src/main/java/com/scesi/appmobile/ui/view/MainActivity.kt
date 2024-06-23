package com.scesi.appmobile.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scesi.appmobile.R
import com.scesi.appmobile.ui.viewmodel.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.network.RetrofitClient

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns
        movieAdapter = MovieAdapter()
        recyclerView.adapter = movieAdapter

        // Initialize ViewModel
        val factory = MovieViewModelFactory(MovieRepository(RetrofitClient.apiService))
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.movies.observe(this, Observer { movies ->
            movieAdapter.submitList(movies)
        })

        // Add scroll listener for pagination
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
