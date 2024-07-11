package com.scesi.appmobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.databinding.FragmentDetailBinding
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.utils.Constantes

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
        movieViewModel = MovieViewModel.getInstance(application.repository)

        val movie: MovieEntity? = arguments?.getSerializable("movie") as? MovieEntity
        movie?.let {
            binding.movie = it
            val posterUrl = "${Constantes.IMG_BASE_URL}${it.posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .into(binding.imageViewBackdrop)

            binding.buttonBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            updateFavoriteButtonText(it.isFavorite)
            binding.buttonFavorite.setOnClickListener { view ->
                val newFavoriteStatus = !it.isFavorite
                it.isFavorite = newFavoriteStatus
                movieViewModel.updateFavoriteStatus(it.id, newFavoriteStatus)

                val message = if (newFavoriteStatus) {
                    "Se agregó la película en la sección de favoritos."
                } else {
                    "Se eliminó la película de la sección de favoritos."
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                updateFavoriteButtonText(newFavoriteStatus)
            }
        }
    }

    private fun updateFavoriteButtonText(isFavorite: Boolean) {
        if (isFavorite) {
            binding.buttonFavorite.text = "Eliminar de favoritos"
        } else {
            binding.buttonFavorite.text = "Agregar a favoritos"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
