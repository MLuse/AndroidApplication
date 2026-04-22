package com.example.jokesapp.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.JOKE_PROVIDER_CONTENT_URI
import com.example.jokesapp.R
import com.example.jokesapp.model.Joke

class JokePagerAdapter(
    private val context: Context,
    private val jokes: MutableList<Joke>
) : RecyclerView.Adapter<JokePagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_joke_detail, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val joke = jokes[position]
        holder.bind(joke)

        holder.ivFavorite.setOnClickListener {
            toggleFavorite(position)
        }

        holder.ivShare.setOnClickListener {
            shareJoke(joke)
        }
    }

    private fun toggleFavorite(position: Int) {
        val joke = jokes[position]
        joke.favorite = !joke.favorite
        context.contentResolver.update(
            ContentUris.withAppendedId(JOKE_PROVIDER_CONTENT_URI, joke._id ?: return),
            ContentValues().apply { put(Joke::favorite.name, joke.favorite) },
            null,
            null
        )
        notifyItemChanged(position)
    }

    private fun shareJoke(joke: Joke) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, joke.joke)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_joke)))
    }

    override fun getItemCount() = jokes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
        private val tvLanguage = itemView.findViewById<TextView>(R.id.tvLanguage)
        private val tvJoke = itemView.findViewById<TextView>(R.id.tvJoke)
        val ivFavorite = itemView.findViewById<ImageView>(R.id.ivFavorite)
        val ivShare = itemView.findViewById<ImageView>(R.id.ivShare)

        fun bind(joke: Joke) {
            tvCategory.text = joke.category
            tvLanguage.text = joke.lang
            tvJoke.text = joke.joke
            ivFavorite.setImageResource(
                if (joke.favorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
    }
}
