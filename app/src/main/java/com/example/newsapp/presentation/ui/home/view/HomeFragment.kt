package com.example.newsapp.presentation.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.BuildConfig
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.presentation.commons.Resource
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import com.example.newsapp.presentation.ui.home.utils.NewsArticleAdapter
import com.example.newsapp.presentation.ui.home.viewmodel.HomeViewModel
import com.example.newsapp.presentation.utils.addDecorator
import com.example.newsapp.presentation.utils.createDecorator
import com.example.newsapp.presentation.utils.dp
import com.example.newsapp.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var articleAdapter: NewsArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            onHamBurgerClick = clickListener
            title = "NewsApp"
        }
        setupView()
        observers()
    }

    private fun setupView() {
        binding.apply {
            rvArticles.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = articleAdapter
            }
            articleAdapter.setOnArticleClick(articleClickListener)
        }
    }

    private fun observers() {
        viewModel.trendingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    initLoader()
                }

                is Resource.Error -> {
                    finishLoader()
                    if (BuildConfig.DEBUG) showToast("error: ${it.message}")
                }

                is Resource.Success -> {
                    finishLoader()
                    articleAdapter.submitList(it.data)
                }
            }
        }
    }

    private val progressBar: AlertDialog? by lazy {
        AlertDialog.Builder(requireContext(), R.style.ProgressDialog)
            .setView(R.layout.layout_loading)
            .setCancelable(false)
            .create()
    }

    private fun initLoader() = progressBar?.takeIf { !it.isShowing }?.show()
    private fun finishLoader() = progressBar?.takeIf { it.isShowing }?.dismiss()

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    private val articleClickListener by lazy {
        object : NewsArticleAdapter.ArticleClickListener {
            override fun onClick(article: NewsArticle) {
                showToast(article.title.toString())
                val action = HomeFragmentDirections.actionHomeFragmentToReadingFragment(article)
                findNavController().navigate(action)
            }
        }
    }

    private val clickListener by lazy {
        View.OnClickListener {
            when (it) {
                binding.hamBurger -> showToast("hamburger clicked")
            }
        }
    }

    companion object {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}