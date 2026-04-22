package com.example.jokesapp.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.jokesapp.JOKE_PROVIDER_CONTENT_URI
import com.example.jokesapp.JokeReceiver
import com.example.jokesapp.framework.sendBroadcast
import com.example.jokesapp.model.Joke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class JokeFetcher(private val context: Context) {

    private val jokeApi: JokeApi = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JokeApi::class.java)

    fun fetchItems(count: Int = 10) {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(count) {
                runCatching { jokeApi.fetchJoke() }
                    .onSuccess { response ->
                        if (!response.error && !response.joke.isNullOrBlank()) {
                            context.contentResolver.insert(
                                JOKE_PROVIDER_CONTENT_URI,
                                ContentValues().apply {
                                    put(Joke::apiId.name, response.id)
                                    put(Joke::category.name, response.category)
                                    put(Joke::type.name, response.type)
                                    put(Joke::joke.name, response.joke)
                                    put(Joke::safe.name, response.safe)
                                    put(Joke::lang.name, response.lang)
                                    put(Joke::nsfw.name, response.flags.nsfw)
                                    put(Joke::religious.name, response.flags.religious)
                                    put(Joke::political.name, response.flags.political)
                                    put(Joke::racist.name, response.flags.racist)
                                    put(Joke::sexist.name, response.flags.sexist)
                                    put(Joke::explicit.name, response.flags.explicit)
                                    put(Joke::favorite.name, false)
                                }
                            )
                        }
                    }
                    .onFailure { Log.e("JokeFetcher", it.toString(), it) }
            }
            context.sendBroadcast<JokeReceiver>()
        }
    }
}
