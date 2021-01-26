package com.miko.gamesss.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameListResponse(

    @field:SerializedName("results")
    val results: List<GameListItem>
) : Parcelable
