package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.databinding.ActivityMainBinding
import com.scesi.appmobile.ui.viewmodel.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieRepository(ApiClient.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.movies.observe(this, Observer { movies ->
            binding.recyclerView.adapter = MovieAdapter(movies)
        })

        viewModel.fetchPopularMovies()
    }
}
