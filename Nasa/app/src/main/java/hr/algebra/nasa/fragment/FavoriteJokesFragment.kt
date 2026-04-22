package com.example.jokesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jokesapp.JOKE_ID
import com.example.jokesapp.JokePagerActivity
import com.example.jokesapp.adapter.JokeAdapter
import com.example.jokesapp.databinding.FragmentFavoritesBinding
import com.example.jokesapp.framework.fetchFavoriteJokes
import com.example.jokesapp.framework.startActivity

class FavoriteJokesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderFavorites()
    }

    override fun onResume() {
        super.onResume()
        renderFavorites()
    }

    private fun renderFavorites() {
        val jokes = requireContext().fetchFavoriteJokes()
        binding.rvFavorites.apply {
            adapter = JokeAdapter(requireContext(), jokes) { jokeId ->
                requireContext().startActivity<JokePagerActivity>(JOKE_ID, jokeId)
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
