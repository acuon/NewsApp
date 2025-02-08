package com.example.newsapp.presentation.ui.reading.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentReadingBinding
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import com.example.newsapp.presentation.utils.humanFriendlyTime
import com.example.newsapp.presentation.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingFragment : Fragment() {

    private var _binding: FragmentReadingBinding? = null
    private val binding get() = _binding!!

    private lateinit var args: ReadingFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = ReadingFragmentArgs.fromBundle(requireArguments())
        val article: NewsArticle? = if (::args.isInitialized) args.article else null

        binding.apply {
            onBackClick = clickListener
            articleImage.loadImage(article?.urlToImage)
            articleDate.text = article?.publishedAt.humanFriendlyTime()
            articleSource.text = article?.sourceName
            articleHeading.text = article?.title
            articleContent.text = article?.content
            articleDescription.text = article?.description
        }
    }

    private val clickListener by lazy {
        View.OnClickListener {
            when (it) {
                binding.backArrow -> findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}