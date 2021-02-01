package com.miko.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GameListItem(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("background_image")
    val backgroundImage: String?
) : Parcelable