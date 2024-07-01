package com.scesi.appmobile.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.scesi.appmobile.utils.Constantes

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load("${Constantes.IMG_BASE_URL}$url")
        .into(view)
}

@BindingAdapter("titleText")
fun setTitle(view: TextView, title: String?) {
    view.text = title
}
