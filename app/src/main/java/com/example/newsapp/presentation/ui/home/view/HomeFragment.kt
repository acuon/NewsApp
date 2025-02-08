package com.example.newsapp.presentation.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.presentation.commons.Resource
import com.example.newsapp.presentation.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

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
            onHamBurgerClick = View.OnClickListener {
                Toast.makeText(requireContext(), "fuck", Toast.LENGTH_SHORT).show()
            }
            title = "NewsApp"
        }

        observers()
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    private fun observers() {
        viewModel.trendingResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), "error: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Success -> {
                    Toast.makeText(requireContext(), "success: ${it.data?.size}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val clickListener by lazy {
        View.OnClickListener {

        }
    }

    companion object {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}