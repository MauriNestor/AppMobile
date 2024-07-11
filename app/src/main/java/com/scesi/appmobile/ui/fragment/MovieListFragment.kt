package com.scesi.appmobile.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.databinding.FragmentMovieListBinding
import com.scesi.appmobile.ui.adapter.MovieAdapter
import com.scesi.appmobile.ui.viewmodel.MovieViewModel

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
        movieAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        binding.recyclerView.adapter = movieAdapter

        viewModel = MovieViewModel.getInstance(MyApplication.getRepository())

        viewModel.getMoviesLiveData(endpoint).observe(viewLifecycleOwner, Observer { movieList ->
            movieAdapter.submitList(movieList)
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

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (!isConnected()) {
                    Snackbar.make(binding.root, "No internet connection. Loading from database...", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun navigateToDetail(movie: MovieEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie)
        findNavController().navigate(action)
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

    private fun isConnected(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
