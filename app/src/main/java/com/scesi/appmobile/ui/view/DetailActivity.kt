package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.scesi.appmobile.core.Constantes
import com.scesi.appmobile.databinding.ActivityDetailBinding
import com.scesi.appmobile.data.model.Result

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Result>("movie")
        // Set the movie details in the views
        movie?.let {
            binding.textViewTitle.text = it.title
            binding.textViewOverview.text = it.overview
            binding.textViewRating.text = it.vote_average.toString()

            Glide.with(this)
                .load("${Constantes.IMG_BASE_URL}${it.poster_path}")
                .into(binding.imageView)

            // Add any other data you find interesting
            binding.textViewReleaseDate.text = it.release_date
        }

        // Set the back button to finish the activity
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
