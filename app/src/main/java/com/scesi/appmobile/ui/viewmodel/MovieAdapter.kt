package com.scesi.appmobile.ui.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scesi.appmobile.databinding.ItemMovieBinding
import com.scesi.appmobile.data.model.Result
import com.scesi.appmobile.core.Constantes

class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: List<Result> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.binding.textViewTitle.text = movie.title

        val posterUrl = "${Constantes.IMG_BASE_URL}${movie.poster_path}"
        Glide.with(holder.binding.root.context)
            .load(posterUrl)
            .into(holder.binding.imageView)
    }

    fun submitList(movies: List<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}

class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)