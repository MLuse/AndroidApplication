package com.example.jokesapp.model

import com.google.gson.annotations.SerializedName

data class JokeResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("category") val category: String,
    @SerializedName("type") val type: String,
    @SerializedName("joke") val joke: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("safe") val safe: Boolean,
    @SerializedName("lang") val lang: String,
    @SerializedName("flags") val flags: JokeFlags
)

data class JokeFlags(
    @SerializedName("nsfw") val nsfw: Boolean,
    @SerializedName("religious") val religious: Boolean,
    @SerializedName("political") val political: Boolean,
    @SerializedName("racist") val racist: Boolean,
    @SerializedName("sexist") val sexist: Boolean,
    @SerializedName("explicit") val explicit: Boolean
)
