package com.example.newsapp.presentation.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R

fun ImageView.loadImage(url: Any?, placeholder: Int = R.drawable.placeholder) {
    val options = RequestOptions()
        .placeholder(placeholder)
        .error(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(url)
        .apply(options)
        .into(this)
}