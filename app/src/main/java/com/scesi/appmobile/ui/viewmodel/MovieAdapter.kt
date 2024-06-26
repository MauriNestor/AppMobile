package com.scesi.appmobile.ui.viewmodel

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scesi.appmobile.data.model.Result
import com.scesi.appmobile.databinding.ItemMovieBinding
import com.scesi.appmobile.ui.view.DetailActivity
import com.scesi.appmobile.core.Constantes

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = listOf<Result>()

    fun submitList(movieList: List<Result>) {
        movies = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Result) {
            binding.movie = movie
            binding.executePendingBindings()

            val posterUrl = "${Constantes.IMG_BASE_URL}${movie.poster_path}"
            Glide.with(binding.imageView.context)
                .load(posterUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("movie", movie)
                context.startActivity(intent)
            }
        }
    }
}
