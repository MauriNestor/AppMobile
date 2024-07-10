package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.scesi.appmobile.MyApplication
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

        // Obtener la instancia del ViewModel usando el singleton de MyApplication
        viewModel = MovieViewModel.getInstance(MyApplication.getRepository())

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
