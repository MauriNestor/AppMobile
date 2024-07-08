package com.scesi.appmobile.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.scesi.appmobile.MyApplication
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.databinding.ActivityDetailBinding
import com.scesi.appmobile.ui.viewmodel.MovieViewModel
import com.scesi.appmobile.ui.viewmodel.MovieViewModelFactory
import com.scesi.appmobile.utils.Constantes

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val application = application as MyApplication
        val repository = application.repository
        val factory = MovieViewModelFactory(repository)
        movieViewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        val movie: MovieEntity? = intent.getSerializableExtra("movie") as? MovieEntity
        movie?.let {
            binding.movie = it
            val posterUrl = "${Constantes.IMG_BASE_URL}${it.posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .into(binding.imageViewBackdrop)

            binding.buttonBack.setOnClickListener {
                finish()
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
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
}
