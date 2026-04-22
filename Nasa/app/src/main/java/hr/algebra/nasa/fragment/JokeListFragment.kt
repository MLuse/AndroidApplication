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
import com.example.jokesapp.databinding.FragmentJokeListBinding
import com.example.jokesapp.framework.fetchJokes
import com.example.jokesapp.framework.startActivity

class JokeListFragment : Fragment() {

    private var _binding: FragmentJokeListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJokeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderJokes()
    }

    override fun onResume() {
        super.onResume()
        renderJokes()
    }

    private fun renderJokes() {
        val jokes = requireContext().fetchJokes()
        binding.rvJokes.apply {
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
