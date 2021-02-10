package com.miko.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class GameListItem(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("metacritic")
    val metacritic: Int,

    @field:SerializedName("background_image")
    val backgroundImage: String?
)