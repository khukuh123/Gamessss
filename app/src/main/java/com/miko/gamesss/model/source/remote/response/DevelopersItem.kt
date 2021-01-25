package com.miko.gamesss.model.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DevelopersItem(

    @field:SerializedName("name")
    val name: String
) : Parcelable