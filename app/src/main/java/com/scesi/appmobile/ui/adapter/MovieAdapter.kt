package com.scesi.appmobile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scesi.appmobile.R
import com.scesi.appmobile.databinding.ItemMovieBinding
import com.scesi.appmobile.domain.model.Movie
import com.scesi.appmobile.utils.Constants

class MovieAdapter(private val onItemClick: (Movie) -> Unit) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()

            val posterUrl = "${Constants.IMG_BASE_URL}${movie.posterPath}"

            Glide.with(binding.imageView.context)
                .load(R.drawable.notfoundimg)
                .into(binding.imageView)

            Glide.with(binding.imageView.context)
                .load(posterUrl)
                .apply(RequestOptions().placeholder(R.drawable.notfoundimg).error(R.drawable.error))
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
