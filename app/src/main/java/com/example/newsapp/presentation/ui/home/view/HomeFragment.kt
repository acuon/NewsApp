package com.example.newsapp.presentation.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import com.example.newsapp.presentation.ui.home.utils.NewsArticleAdapter
import com.example.newsapp.presentation.ui.home.viewmodel.HomeViewModel
import com.example.newsapp.presentation.utils.handleResourceState
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
            handleResourceState(it,
                onLoading = {
                    initLoader()
                },
                onSuccess = { state ->
                    finishLoader()
                    articleAdapter.submitList(state.data)
                },
                onError = { message, state ->
                    finishLoader()
                },
                retryAction = {
                    viewModel.fetchTrendingNews()
                }
            )
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

    private val articleClickListener by lazy {
        object : NewsArticleAdapter.ArticleClickListener {
            override fun onClick(article: NewsArticle) {
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