package com.example.jokesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jokesapp.databinding.FragmentJokeCategoryBinding
import com.example.jokesapp.framework.fetchJokes

class JokeCategoryFragment : Fragment() {

    private var _binding: FragmentJokeCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJokeCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = requireContext().fetchJokes().map { it.category }.distinct().sorted()
        binding.tvCategories.text = categories.joinToString(separator = "\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
