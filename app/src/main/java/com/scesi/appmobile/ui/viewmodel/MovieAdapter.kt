package com.scesi.appmobile.ui.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scesi.appmobile.data.model.Result
import com.scesi.appmobile.databinding.ItemMovieBinding

class MovieAdapter(private val movies: List<Result>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Result) {
            binding.movieTitle.text = movie.title
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(binding.moviePoster)
        }
    }
}
