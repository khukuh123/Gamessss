package com.miko.gamesss.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameDetailResponse(

    @field:SerializedName("background_image")
    val backgroundImage: String?,

    @field:SerializedName("website")
    val website: String,

    @field:SerializedName("developers")
    val developers: List<DevelopersItem>,

    @field:SerializedName("genres")
    val genres: List<GenresItem>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("publishers")
    val publishers: List<PublishersItem>,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("playtime")
    val playtime: Int,

    @field:SerializedName("parent_platforms")
    val parentPlatforms: List<ParentPlatformsItem>,

    @field:SerializedName("description_raw")
    val descriptionRaw: String,

    @field:SerializedName("released")
    val released: String?
) : Parcelable