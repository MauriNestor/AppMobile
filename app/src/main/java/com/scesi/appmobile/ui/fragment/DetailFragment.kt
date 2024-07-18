package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.FragmentDetailBinding
import com.scesi.appmobile.ui.viewmodel.DetailViewModel
import com.scesi.appmobile.utils.Constants

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = MyApplication.getInstance()
        val repository = application.repository

        detailViewModel = DetailViewModel.getInstance(repository)

        val movieId = arguments?.getInt("movieId")

        if (movieId != null) {
            detailViewModel.fetchMovieDetails(movieId)
        }

        detailViewModel.movieDetails.observe(viewLifecycleOwner, Observer { movie ->
            binding.movie = movie

            val posterUrl = "${Constants.IMG_BASE_URL}${movie.posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .into(binding.imageViewBackdrop)

            (activity as? AppCompatActivity)?.supportActionBar?.title = movie.title

            updateFavoriteIcon(movie.isFavorite)
            binding.fabFavorite.setOnClickListener {
                val newFavoriteStatus = !movie.isFavorite
                movie.isFavorite = newFavoriteStatus
                detailViewModel.updateFavoriteStatus(movie.id, newFavoriteStatus)

                val message = if (newFavoriteStatus) {
                    "Se agregó la película en la sección de favoritos."
                } else {
                    "Se eliminó la película de la sección de favoritos."
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                updateFavoriteIcon(newFavoriteStatus)
            }
        })
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.corazon_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.corazon_25)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
