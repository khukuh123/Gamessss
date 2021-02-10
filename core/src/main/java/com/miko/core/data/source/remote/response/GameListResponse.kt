package com.miko.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameListResponse(

    @field:SerializedName("results")
    val results: List<GameListItem>
)
