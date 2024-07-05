package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.scesi.appmobile.data.local.entity.MovieEntity
import com.scesi.appmobile.databinding.ActivityDetailBinding
import com.scesi.appmobile.utils.Constantes

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        }
    }
}
