package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.FragmentDetailBinding
import com.scesi.appmobile.domain.model.Movie
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.utils.Constants

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieViewModel: MovieViewModel

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
        val context = requireContext()

        movieViewModel = MovieViewModel.getInstance(repository, context)

        val movieId = arguments?.getInt("movieId")
        val title = arguments?.getString("title")
        val overview = arguments?.getString("overview")
        val posterPath = arguments?.getString("posterPath")
        val voteAverage = arguments?.getFloat("voteAverage")
        val releaseDate = arguments?.getString("releaseDate")
        val popularity = arguments?.getFloat("popularity")
        val category = arguments?.getString("category")
        val isFavorite = arguments?.getBoolean("isFavorite")

        if (movieId != null && title != null && overview != null && posterPath != null && voteAverage != null && releaseDate != null && popularity != null && category != null && isFavorite != null) {
            val movie = Movie(
                id = movieId,
                title = title,
                overview = overview,
                posterPath = posterPath,
                voteAverage = voteAverage.toDouble(),
                releaseDate = releaseDate,
                popularity = popularity.toDouble(),
                category = category,
                isFavorite = isFavorite
            )
            binding.movie = movie

            val posterUrl = "${Constants.IMG_BASE_URL}${posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .into(binding.imageViewBackdrop)

            (activity as? AppCompatActivity)?.supportActionBar?.title = title

            updateFavoriteIcon(movie.isFavorite)
            binding.fabFavorite.setOnClickListener {
                val newFavoriteStatus = !movie.isFavorite
                movie.isFavorite = newFavoriteStatus
                movieViewModel.updateFavoriteStatus(movieId, newFavoriteStatus)

                val message = if (newFavoriteStatus) {
                    "Se agregó la película en la sección de favoritos."
                } else {
                    "Se eliminó la película de la sección de favoritos."
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                updateFavoriteIcon(newFavoriteStatus)
            }
        }
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
