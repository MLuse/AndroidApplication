package com.example.jokesapp.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.JOKE_PROVIDER_CONTENT_URI
import com.example.jokesapp.R
import com.example.jokesapp.model.Joke

class JokeAdapter(
    private val context: Context,
    private val jokes: MutableList<Joke>,
    private val onJokeSelected: (Long) -> Unit
) : RecyclerView.Adapter<JokeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val joke = jokes[position]
        holder.bind(joke)

        holder.itemView.setOnClickListener {
            joke._id?.let(onJokeSelected)
        }

        holder.itemView.setOnLongClickListener {
            deleteJoke(position)
            true
        }
    }

    private fun deleteJoke(position: Int) {
        val joke = jokes[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(JOKE_PROVIDER_CONTENT_URI, joke._id ?: return),
            null,
            null
        )
        jokes.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = jokes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
        private val tvJoke = itemView.findViewById<TextView>(R.id.tvJoke)
        private val ivFavorite = itemView.findViewById<ImageView>(R.id.ivFavorite)

        fun bind(joke: Joke) {
            tvCategory.text = joke.category
            tvJoke.text = joke.joke
            ivFavorite.setImageResource(
                if (joke.favorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
    }
}
