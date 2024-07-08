package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.databinding.FragmentFavoritesBinding
import com.scesi.appmobile.ui.adapter.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val repository = (application as MyApplication).repository
        val factory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        movieAdapter = MovieAdapter()

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }

        viewModel.favoriteMovies.observe(viewLifecycleOwner, { movies ->
            movieAdapter.submitList(movies)
        })

        viewModel.getFavoriteMovies()
    }
    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteMovies()
    }
}
