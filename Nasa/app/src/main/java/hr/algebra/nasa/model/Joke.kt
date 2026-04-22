package com.example.jokesapp.model

data class Joke(
    var _id: Long?,
    val apiId: Int,
    val category: String,
    val type: String,
    val joke: String,
    val safe: Boolean,
    val lang: String,
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean,
    var favorite: Boolean
)
