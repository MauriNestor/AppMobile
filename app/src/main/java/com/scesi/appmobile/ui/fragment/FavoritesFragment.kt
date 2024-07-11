package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.databinding.FragmentFavoritesBinding
import com.scesi.appmobile.ui.adapter.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel

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

        viewModel = MovieViewModel.getInstance(MyApplication.getRepository())

        movieAdapter = MovieAdapter { movie -> navigateToDetail(movie) }

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }

        viewModel.favoriteMovies.observe(viewLifecycleOwner, { movies ->
            movieAdapter.submitList(movies)
        })

        viewModel.getFavoriteMovies()
    }

    private fun navigateToDetail(movie: MovieEntity) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(movie)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteMovies()
    }
}
