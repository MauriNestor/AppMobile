package com.scesi.appmobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scesi.appmobile.ui.viewmodel.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory
import com.scesi.appmobile.data.model.MovieRepository
import com.scesi.appmobile.databinding.FragmentMovieListBinding
import com.scesi.appmobile.network.RetrofitClient

class MovieListFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var endpoint: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        endpoint = arguments?.getString("endpoint") ?: "now_playing"

        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        movieAdapter = MovieAdapter()
        binding.recyclerView.adapter = movieAdapter

        val factory = MovieViewModelFactory(MovieRepository(RetrofitClient.apiService))
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movieAdapter.submitList(movies)
        })

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem + 3 >= totalItemCount) {
                    viewModel.loadNextPage(endpoint)
                }
            }
        })

        viewModel.getMovies(endpoint)
    }

    companion object {
        @JvmStatic
        fun newInstance(endpoint: String) =
            MovieListFragment().apply {
                arguments = Bundle().apply {
                    putString("endpoint", endpoint)
                }
            }
    }
}
