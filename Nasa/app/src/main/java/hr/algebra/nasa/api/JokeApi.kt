package com.example.jokesapp.api

import com.example.jokesapp.model.JokeResponse
import retrofit2.http.GET

const val API_URL = "https://v2.jokeapi.dev/"

interface JokeApi {
    @GET("joke/Any")
    suspend fun fetchJoke(): JokeResponse
}
