package com.example.jokesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jokesapp.adapter.JokePagerAdapter
import com.example.jokesapp.databinding.ActivityJokePagerBinding
import com.example.jokesapp.framework.fetchJokes
import com.example.jokesapp.model.Joke

const val JOKE_ID = "com.example.jokesapp.joke_id"

class JokePagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJokePagerBinding
    private lateinit var jokes: MutableList<Joke>
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokePagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initPager()
    }

    private fun initPager() {
        binding.viewPager2.adapter = JokePagerAdapter(this, jokes)
        binding.viewPager2.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val selectedId = intent.getLongExtra(JOKE_ID, -1L)
        jokes = fetchJokes()
        position = jokes.indexOfFirst { it._id == selectedId }.takeIf { it >= 0 } ?: 0
    }
}
