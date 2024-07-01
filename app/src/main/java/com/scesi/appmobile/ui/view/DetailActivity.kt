package com.scesi.appmobile.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.scesi.appmobile.data.model.Result
import com.scesi.appmobile.databinding.ActivityDetailBinding
import com.scesi.appmobile.utils.Constantes

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie: Result? = intent.getParcelableExtra("movie")
        movie?.let {
            binding.movie = it
            val posterUrl = "${Constantes.IMG_BASE_URL}${it.backdrop_path}"
            Glide.with(this)
                .load(posterUrl)
                .into(binding.imageViewBackdrop)

            binding.buttonBack.setOnClickListener {
                finish()
            }
        }
    }
}
