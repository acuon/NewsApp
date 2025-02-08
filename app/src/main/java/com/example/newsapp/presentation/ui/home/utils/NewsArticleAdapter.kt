package com.example.newsapp.presentation.ui.home.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemLayoutNewsArticleBinding
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import com.example.newsapp.presentation.utils.gone
import com.example.newsapp.presentation.utils.humanFriendlyTime
import com.example.newsapp.presentation.utils.inflater
import com.example.newsapp.presentation.utils.loadImage
import com.example.newsapp.presentation.utils.show
import javax.inject.Inject

class NewsArticleAdapter @Inject constructor() :
    RecyclerView.Adapter<NewsArticleAdapter.ViewHolder>() {

    private val list = ArrayList<NewsArticle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutNewsArticleBinding.inflate(
                parent.inflater(),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    private lateinit var listener: ArticleClickListener

    fun setOnArticleClick(callback: ArticleClickListener) {
        listener = callback
    }

    fun submitList(data: List<NewsArticle?>?) {
        if (!data.isNullOrEmpty()) {
            list.clear()
            list.addAll(data.filterNotNull())
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemLayoutNewsArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticle, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    if (::listener.isInitialized) {
                        listener.onClick(article)
                    }
                }
                articleImage.loadImage(article.urlToImage)
                articleHeading.text = article.title
                articleSource.text = article.sourceName
                articleDate.text = buildString { append(article.publishedAt?.humanFriendlyTime()) }
                if (position == list.size - 1) separator.gone() else separator.show()
            }
        }
    }

    interface ArticleClickListener {
        fun onClick(article: NewsArticle)
    }
}